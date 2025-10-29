import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { UserService } from '../../services/user/user';
import { FormsModule, NgForm } from '@angular/forms';
import { LoginUser } from '../../models/User';

@Component({
  selector: 'app-sign-in',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, FormsModule],
  templateUrl: './sign-in.html',
  styleUrl: './sign-in.css',
})
export class SignIn {
  constructor(private userService: UserService, private router: Router) { }
  
    onSubmit(form: NgForm) {
      if (form.valid) {
        const userData: LoginUser = form.value;
        console.log('Form Data:', userData);
        this.userService.login(userData).subscribe({
          next: (res: any) => {
            localStorage.setItem('jwt', res.jwt);
            localStorage.setItem('userEmail', userData.email);
            console.log('Logged in successfully:', res.jwt);
            this.router.navigate(['/accounts']);
          },
          error: (err) => {
            console.error('Login failed:', err);
            alert('Invalid credentials.');
          }
        });
        this.router.navigate(['/signin']);
      }
    }
}
