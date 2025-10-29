// import { TestBed } from '@angular/core/testing';
// import { Router } from '@angular/router';
// import { AuthGuard } from './auth.guard';
// import { AuthService } from '../services/auth.service';

// describe('AuthGuard', () => {
//   let guard: AuthGuard;
//   let authService: jasmine.SpyObj<AuthService>;
//   let router: jasmine.SpyObj<Router>;

//   beforeEach(() => {
//     const authSpy = jasmine.createSpyObj('AuthService', ['isAuthenticated']);
//     const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

//     TestBed.configureTestingModule({
//       providers: [
//         AuthGuard,
//         { provide: AuthService, useValue: authSpy },
//         { provide: Router, useValue: routerSpy }
//       ]
//     });

//     guard = TestBed.inject(AuthGuard);
//     authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
//     router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
//   });

//   it('should allow navigation when authenticated', () => {
//     authService.isAuthenticated.and.returnValue(true);
//     expect(guard.canActivate()).toBeTrue();
//   });

//   it('should redirect to signin when not authenticated', () => {
//     authService.isAuthenticated.and.returnValue(false);
//     guard.canActivate();
//     expect(router.navigate).toHaveBeenCalledWith(['/signin']);
//   });
// });
