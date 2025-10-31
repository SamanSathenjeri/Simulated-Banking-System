import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { UserService } from '../../services/user/user';
import { EnvelopeService } from '../../services/envelope/envelope';
import { Envelope, Signer } from '../../models/Envelope';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header implements OnInit {
  constructor(private userService: UserService, private envelopeService: EnvelopeService, private router: Router) {}
  name: string | null = null;
  hasUnsignedDocuments = false;

  ngOnInit(): void {
    this.setUser();
    this.findUnsignedDocuments();
  }

  setUser() {
    this.userService.getName().subscribe({
      next: (data: string) => {
        console.log(data);
        this.name = data;
      },
      error: (err) => {
        console.error('Error fetching accounts:', err);
      }
    });
  }

  findUnsignedDocuments() {
    this.userService.getEnvelopes().subscribe({
      next: (data: Signer[]) => {
        for (const envelope of data){
          this.getEnvelopeStatus(envelope.envelopeId)
        }
      },
      error: (err) => {
        console.error('Error fetching envelopes:', err);
      }
    });
  }

  getEnvelopeStatus(envelopeId: number){
    this.envelopeService.getEnvelopeById(envelopeId).subscribe({
      next: (response: Envelope) => {
        if (response.status == 'PENDING'){
          this.hasUnsignedDocuments = true;
        }
      },
      error: (error) => {
        console.error('Envelope failed', error);
      }
    });
  }

  logout() {
    this.userService.logout();
    this.router.navigate(['/signin']);
  }
}