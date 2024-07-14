import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ServerService } from './service/server.service';
import { BehaviorSubject, Observable, catchError, map, of, startWith } from 'rxjs';
import { AppState } from './interface/app-state';
import { CustomResponse } from './interface/custom.response';
import { DataState } from './enum/data.state.enum';
import { CommonModule } from '@angular/common';
import { Status } from './enum/status.enum';
import { FormsModule, NgForm } from '@angular/forms';
import { Server } from './interface/server';
import { jsPDF } from 'jspdf';
import html2canvas from 'html2canvas';
import { NotificationService } from './service/notification.service';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {

  appState$: Observable<AppState<CustomResponse>>;

  readonly DataState = DataState;

  readonly Status = Status;


  selectedStatus: Status = Status.ALL;

  private filterSubject = new BehaviorSubject<string>('');

  private dataSubject = new BehaviorSubject<CustomResponse>(null);
  filterStatus$ = this.filterSubject.asObservable();

  //for showing spinner on the save button
  //isLoading is a subject
  //and that variable used to set the value on this observable(isLoading$)
  private isLoading = new BehaviorSubject<boolean>(false);
  //and here we making an observable out of the subject(isLaoding)
  //this variable can be used in the UI (.html)
  isLoading$ = this.isLoading.asObservable();

  constructor(private serverService: ServerService,
    private notifier: NotificationService) { }

  //show success notification to the user


  ngOnInit(): void {

    this.appState$ = this.serverService.servers$
      .pipe(
        map(response => {
          //this peace of code (...response, data: {servers: response.data.servers.reverse()) is to put the last added server on top
          this.dataSubject.next(response);
          //showing notification message
         // this.notifier.onDefault(response.message);
          return { dataState: DataState.LOADED_STATE, appData: { ...response, data: { servers: response.data.servers.reverse() } } }
        }),
        startWith({ dataState: DataState.LOADING_STATE }),
        catchError((error: string) => {
          this.notifier.onError(error);

          return of({ dataState: DataState.ERROR_STATE, error })
        })

      );

  }

  //this function for activate spinning while pinging or stop if pinging stop
  pingServer(ipAddress: string): void {
    //first the pinged server will have the spinner instead of the router icon
    this.filterSubject.next(ipAddress);
    //then we call the ping function from the service
    //and pass the ipAddress to ping the server in the backend
    this.appState$ = this.serverService.ping$(ipAddress)
      .pipe(
        map(response => {
          const index = //this two lines only returns an index
            this.dataSubject.value.data.servers.findIndex(server =>
              server.id === response.data.server.id
            );
          this.dataSubject.value.data.servers[index] = response.data.server;

          this.notifier.onDefault(response.message);

          //after processing the ping we stop showing the spinner and show the router icon
          this.filterSubject.next('');

          return { dataState: DataState.LOADED_STATE, appData: this.dataSubject.value }
        }),
        startWith({ dataState: DataState.LOADED_STATE, appData: this.dataSubject.value }),
        catchError((error: string) => {
          this.notifier.onError(error);

          //even if we have an error we stop the spinner also
          this.filterSubject.next('');
          return of({ dataState: DataState.ERROR_STATE, error })
        })

      );

  }


  saveServer(serverForm: NgForm): void {
    //when the function is called it show spinner on the save button
    this.isLoading.next(true);
    //const statusEnum: Status = Status[selectedStatus as keyof typeof Status];
    this.appState$ = this.serverService.save$(serverForm.value as Server)
      .pipe(
        map(response => {
          this.dataSubject.next(
            { ...response, data: { servers: [response.data.server, ...this.dataSubject.value.data.servers] } }
          );

          this.notifier.onDefault(response.message);

          document.getElementById('closeModal').click();
          //when the modal dismissed the spinner stoppes
          this.isLoading.next(false);
          serverForm.resetForm({ status: this.Status.SERVER_DOWN })
          return { dataState: DataState.LOADED_STATE, appData: this.dataSubject.value }
        }),
        startWith({ dataState: DataState.LOADED_STATE, appData: this.dataSubject.value }),
        catchError((error: string) => {
          this.notifier.onError(error);

          //also the spinner is stopps when catching an error
          this.isLoading.next(false);
          return of({ dataState: DataState.ERROR_STATE, error })
        })

      );

  }

  filterServers(status: Status): void {

    //const statusEnum: Status = Status[selectedStatus as keyof typeof Status];
    this.appState$ = this.serverService.filter$(status, this.dataSubject.value)
      .pipe(
        map(response => {
          this.notifier.onDefault(response.message);

          return { dataState: DataState.LOADED_STATE, appData: response }
        }),
        startWith({ dataState: DataState.LOADED_STATE, appData: this.dataSubject.value }),
        catchError((error: string) => {
          this.notifier.onError(error);

          return of({ dataState: DataState.ERROR_STATE, error })
        })

      );

  }


  deleteServer(server: Server): void {

    //const statusEnum: Status = Status[selectedStatus as keyof typeof Status];
    this.appState$ = this.serverService.delete$(server.id)
      .pipe(
        map(response => {
          this.dataSubject.next(
            {
              ...response, data:
                { servers: this.dataSubject.value.data.servers.filter(s => s.id !== server.id) }
            }
          );
          this.notifier.onDefault(response.message);

          return { dataState: DataState.LOADED_STATE, appData: this.dataSubject.value }
        }),
        startWith({ dataState: DataState.LOADED_STATE, appData: this.dataSubject.value }),
        catchError((error: string) => {
          this.notifier.onError(error);

          return of({ dataState: DataState.ERROR_STATE, error })
        })

      );

  }

  //for printing a report in PDF
  printReport(): void {
    window.print();
    this.notifier.onDefault('report downloaded successfully');
    // Create a new jsPDF instance
    //const doc = new jsPDF();

    // Select the table element
    //const tableSelect = document.getElementById('servers');

    // Calculate the height of the table
    //const tableHeight = tableSelect.clientHeight;

    // Define the page height
    //const pageHeight = doc.internal.pageSize.height;

    // Check if the table fits on a single page
    //if (tableHeight >= pageHeight) {
    // If the table fits on a single page, generate the PDF from the entire table
    //  html2canvas(tableSelect).then(canvas => {
    // const imgData = canvas.toDataURL('image/png');
    // const imgWidth = doc.internal.pageSize.width;
    //const imgHeight = (canvas.height * imgWidth) / canvas.width;
    //doc.addImage(imgData, 'PNG', 0, 0, imgWidth, imgHeight);
    //doc.save('server-report.pdf');
    //});
    //} else {
    // If the table doesn't fit on a single page, display an error message
    //  console.error('Table is too large to fit on a single page');
  }



  //for printing a report in EXCEL
  /*printReport(): void {
    this.notifier.onDefault('Report downloaded');
    // window.print();
    let dataType = 'application/vnd.ms-excel.sheet.macroEnabled.12';
    let tableSelect = document.getElementById('servers');
    let tableHtml = tableSelect.outerHTML.replace(/ /g, '%20');
    let downloadLink = document.createElement('a');
    document.body.appendChild(downloadLink);
    downloadLink.href = 'data:' + dataType + ', ' + tableHtml;
    downloadLink.download = 'server-report.xls';
    downloadLink.click();
    document.body.removeChild(downloadLink);
  }*/

}
