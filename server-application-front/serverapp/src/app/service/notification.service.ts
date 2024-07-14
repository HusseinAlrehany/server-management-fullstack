import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';


@Injectable({ providedIn: 'root' })
export class NotificationService {
  private readonly notifier: ToastrService;

  constructor(private toastrService: ToastrService) {
    this.notifier = toastrService;
  }

  onDefault(message: string): void {
    this.notifier.success( message);
  }

  onSuccess(message: string): void {
    this.notifier.success(message);
  }
  onInfo(message: string): void {
    this.notifier.info(message);
  }
  onError(message: string): void {
    this.notifier.error(message);
  }
  onWarning(message: string): void {
    this.notifier.warning(message);


  }

}

/*enum Type {
  DEFAULT = 'default',
  INFO = 'info',
  SUCCESS = 'success',
  ERROR = 'error',
  WARNING = 'warning'
};*/