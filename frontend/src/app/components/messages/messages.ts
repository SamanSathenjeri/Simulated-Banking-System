import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { HlmTableImports } from '@spartan-ng/helm/table';
import { CommonModule } from '@angular/common';
import { Envelope, Signer } from '../../models/Envelope';
import { EnvelopeService } from '../../services/envelope/envelope';
import { UserService } from '../../services/user/user';
import { Header } from '../header/header';

@Component({
  selector: 'app-messages',
  standalone: true,
  imports: [CommonModule, HlmTableImports, Header],
  templateUrl: './messages.html',
  styleUrl: './messages.css',
})
export class Messages implements OnInit {
  _envelopes: Signer[] = [];
  isLoading = true;
  error: any = null;

  constructor(private envelopeService: EnvelopeService, private userService: UserService, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.fetchEnvelopes();
  }

  fetchEnvelopes() {
    this.userService.getEnvelopes().subscribe({
      next: (data: Signer[]) => {
        this._envelopes = data.reverse();
        this.isLoading = false;
        console.log('Received Envelopes:', this._envelopes);
        this.getEnvelopeStatus();
      },
      error: (err) => {
        this.error = err;
        this.isLoading = false;
        console.error('Error fetching envelopes:', err);
      },
      complete: () => {
        console.log('Envelope stream completed.');
      }
    });
  }

  getEnvelopeStatus(){
    for (const signer of this._envelopes){
      this.envelopeService.getEnvelopeById(signer.envelopeId).subscribe({
        next: (response: Envelope) => {
          console.log('Envelope received successful', response);
          signer.envelopeStatus = response.status;
          this.cdr.detectChanges();
        },
        error: (error) => {
          console.error('Envelope failed', error);
        }
      });
    }
  }

  updateEnvelopeStatus(envelopeId: number){
    this.envelopeService.checkEnvelopeStatus(envelopeId).subscribe({
      next: (response) => {
        console.log('Status Responded', response);
      },
      error: (error) => {
        console.error('Status failed', error);
      }
    });
  }

  signEnvelope(signerId: number){
    const signer = this._envelopes.find(e => e.signerId === signerId);
    if (signer){
      this.envelopeService.signEnvelope(signer.envelopeId).subscribe({
        next: (response) => {
          console.log('Envelope signed successful', response);
          this.updateEnvelopeStatus(signer.envelopeId);
          this.fetchEnvelopes();
          alert(`Envelope #${signer.envelopeId} signed successfully`);
        },
        error: (error) => {
          console.error('Signing failed', error);
        }
      });
    }
  }
}
