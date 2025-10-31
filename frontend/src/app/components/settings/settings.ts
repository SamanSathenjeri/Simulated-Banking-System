import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { UserService } from '../../services/user/user';
import { Header } from '../header/header';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [FormsModule, Header],
  templateUrl: './settings.html',
  styleUrl: './settings.css',
})
export class Settings {
  constructor(private userService: UserService, private router: Router) { }

  changeName(form: NgForm) {
    if (form.valid) {
      const newName = form.value.name;
      console.log('Form Data:', newName);
      this.userService.updateName(newName).subscribe({
        next: (response) => {
          console.log('Name Changed', response);
          form.reset();
        },
        error: (error) => {
          console.error('Error in changing name', error);
        }
      });
    }
  }

  changeEmail(form: NgForm) {
    if (form.valid) {
      const newEmail = form.value.email;
      console.log('Form Data:', newEmail);
      this.userService.updateEmail(newEmail).subscribe({
        next: (response) => {
          console.log('Email Changed', response);
          form.reset();
        },
        error: (error) => {
          console.error('Error in changing email', error);
        }
      });
    }
  }

  changePassword(form: NgForm) {
    if (form.valid) {
      const newPassword = form.value.password;
      console.log('Form Data:', newPassword);
      this.userService.updatePassword(newPassword).subscribe({
        next: (response) => {
          console.log('Password Changed', response);
          form.reset();
        },
        error: (error) => {
          console.error('Error in changing password', error);
        }
      });
    }
  }

  deleteUser(form: NgForm){
    if (confirm('Are you sure you want to delete your account? This cannot be undone.')) {
      this.userService.deleteUser().subscribe({
        next: (response) => {
          console.log('Account Deleted', response);
          form.reset();
        },
        error: (error) => {
          console.error('Error in deleting account', error);
        }
      });
      this.router.navigate(['/signin']);
    }
  }
}
