import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { LoginComponent } from './login/login.component';
import { ContactUsComponent } from './contact-us/contact-us.component';
import { AboutComponent } from './about/about.component';
import { HomeComponent } from './home/home.component';
import { CustomerComponent } from './customer/customer.component';
import { EmployeeComponent } from './employee/employee.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './services/auth.service';

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'customerdashboard', component: CustomerComponent, pathMatch: 'full', canActivate: [AuthGuard] },
  { path: 'employeedashboard', component: EmployeeComponent, pathMatch: 'full', canActivate: [AuthGuard] },
  { path: 'about', component: AboutComponent, pathMatch: 'full' },
  { path: 'contactus', component: ContactUsComponent, pathMatch: 'full' },
  { path: 'login', component: LoginComponent, pathMatch: 'full' },
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
