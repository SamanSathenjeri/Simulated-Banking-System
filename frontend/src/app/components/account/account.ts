import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HlmTableImports } from '@spartan-ng/helm/table';
import { FormsModule } from '@angular/forms';
import { AccountService } from '../../services/account/account';
import { UserService } from '../../services/user/user';
import { AccountModel } from '../../models/AccountModel';
import { Transaction } from '../../models/Envelope';

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [CommonModule, HlmTableImports, FormsModule],
  templateUrl: './account.html',
  styleUrl: './account.css',
})
export class Account implements OnInit {
	isOpen: boolean = false;
	isAccountSettingsModalOpen = false;
	_accounts: string[] = [];
	selectedOptionText: string = 'Your Accounts';
	selectedAccount: AccountModel | null = null;
	balance: string | null = null;
    sentTransactions: Transaction[] = [];
    receivedTransactions: Transaction[] = [];
	receiverAccountId: string | null = null;
	depositAmount = 0;
	transactionAmount = 0;

	constructor(private accountService: AccountService, private userService: UserService, private cdr: ChangeDetectorRef) { }

	ngOnInit(): void {
		this.getAccounts();
		this.selectedOptionText = this._accounts.length === 0 ? 'Your Accounts' : this._accounts[0];
	}

	getAccounts(){
		this.userService.getAccounts().subscribe({
			next: (data: AccountModel[]) => {
				this._accounts = [];
				for (const account of data){
					this._accounts.push(account.accountId.toString());
				}
			},
			error: (err) => {
				console.error('Error fetching accounts:', err);
			}
		});
	}

	setAccountsPage(account: string){
		this.accountService.getAccount(+account).subscribe({
			next: (data: AccountModel) => {
				console.log('Received Account:', data.accountId);
				this.balance = `Balance: $${data.balance}`;
				this.setSentTransactions(data.accountId);
				this.setReceivedTransactions(data.accountId);
				this.selectedAccount = data;
				this.cdr.detectChanges();
			},
			error: (err) => {
				console.error('Error fetching Account:', err);
			}
		});
	}

	setSentTransactions(account: number){
		this.accountService.getAccountSentTransactions(+account).subscribe({
			next: (data: Transaction[]) => {
				console.log('Received Transaction');
				this.sentTransactions = data;
			},
			error: (err) => {
				console.error('Error fetching Account:', err);
			}
		});
	}

	setReceivedTransactions(account: number){
		this.accountService.getAccountReceivedTransactions(+account).subscribe({
			next: (data: Transaction[]) => {
				console.log('Received Transaction');
				this.receivedTransactions = data;
			},
			error: (err) => {
				console.error('Error fetching Account:', err);
			}
		});
	}

	toggleMenu(): void {
		this.isOpen = !this.isOpen;
	}

	selectOption(option: string): void {
		this.selectedOptionText = option;
		this.isOpen = false;
		this.setAccountsPage(option);
	}

	openAccountSettingsModal() {
		this.isAccountSettingsModalOpen = true;
	}

	closeAccountSettingsModal() {
		this.isAccountSettingsModalOpen = false;
	}

	deleteAccount(){
		if (this.selectedAccount != null){
			this.accountService.deleteAccount(+this.selectedAccount?.accountId).subscribe({
				next: () => {
					this.getAccounts();
					this.selectOption('Your Accounts');
					this.closeAccountSettingsModal();
				},
				error: (err) => {
					console.error('Error deleting account:', err);
				}
			});
		}
	}

	deposit() {
		if (this.selectedAccount != null){
			this.accountService.addBalance(+this.selectedAccount?.accountId, this.depositAmount).subscribe({
				next: () => {
					this.closeAccountSettingsModal();
					this.setAccountsPage(this.selectedOptionText);
					this.depositAmount = 0;
				},
				error: (err) => {
					console.error('Error adding balance:', err);
				}
			});
		}
	}

	submitTransaction() {
		if (this.selectedAccount != null && this.receiverAccountId){
			this.accountService.makeTransaction(+this.selectedAccount?.accountId, this.transactionAmount, this.receiverAccountId).subscribe({
				next: () => {
					this.setAccountsPage(this.selectedOptionText);
					this.transactionAmount = 0;
					this.closeAccountSettingsModal();
				},
				error: (err) => {
					console.error('Error making transaction:', err);
				}
			});
		}
		this.closeAccountSettingsModal();
	}

	createNewAccount(){
		this.accountService.addAccount().subscribe({
			next: (data: AccountModel) => {
				this.getAccounts();
				this.selectOption(data.accountId.toString());
			},
			error: (err) => {
				console.error('Error fetching envelopes:', err);
			}
		});
	}
}