import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { UserService } from '../../services/user/user';
import { FormsModule, NgForm } from '@angular/forms';
import { RegistrationUser } from '../../models/User';

@Component({
  selector: 'app-sign-up',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, FormsModule],
  templateUrl: './sign-up.html',
  styleUrl: './sign-up.css',
})
export class SignUp {
  constructor(private userService: UserService, private router: Router) { }

  onSubmit(form: NgForm) {
    if (form.valid) {
      const userData: RegistrationUser = form.value;
      console.log('Form Data:', userData);
      this.userService.createUser(userData).subscribe({
        next: (response) => {
          console.log('Registration successful', response);
          form.reset();
        },
        error: (error) => {
          console.error('Registration failed', error);
        }
      });
      this.router.navigate(['/signin']);
    }
  }
}
