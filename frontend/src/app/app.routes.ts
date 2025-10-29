import { Routes } from '@angular/router';
import { Account } from './components/account/account';
import { Messages } from './components/messages/messages';
import { Settings } from './components/settings/settings';
import { SignIn } from './components/sign-in/sign-in';
import { SignUp } from './components/sign-up/sign-up';
import { AuthGuard } from './guards/auth-guard';

export const routes: Routes = [
  { path: '', redirectTo: '/signin', pathMatch: 'full' },
  { path: 'signin', component: SignIn },
  { path: 'signup', component: SignUp },
  { path: 'accounts', component: Account, canActivate: [AuthGuard] },
  { path: 'messages', component: Messages, canActivate: [AuthGuard] },
  { path: 'settings', component: Settings, canActivate: [AuthGuard] },
];