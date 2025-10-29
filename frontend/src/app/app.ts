import { Component, signal } from '@angular/core';
import { Router, NavigationEnd, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs/operators';
import { Header } from './components/header/header';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  showHeaderFlag = true;

  constructor(private router: Router) {
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        const noHeaderRoutes = ['/signin', '/signup'];
        // log for debug
        console.log('Route changed:', event.urlAfterRedirects);
        this.showHeaderFlag = !noHeaderRoutes.includes(event.urlAfterRedirects);
      });
  }
}
