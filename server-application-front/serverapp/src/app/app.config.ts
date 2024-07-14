import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';
import { provideToastr } from 'ngx-toastr';
import { provideAnimations } from '@angular/platform-browser/animations';

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes),
  provideClientHydration(),
  provideHttpClient(),
  provideToastr(
    {
      closeButton: true,
      positionClass: 'toast-bottom-left',timeOut: 10000
    }
  ),
  provideAnimations()]
};
