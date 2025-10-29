import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';
import { App } from './app/app';
import { routes } from './app/app.routes';
import { provideHttpClient, withInterceptorsFromDi, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './app/interceptors/auth-interceptor';

bootstrapApplication(App, {
  providers: [provideHttpClient(withInterceptorsFromDi()), provideRouter(routes), 
      { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }]
}).catch(err => console.error(err));