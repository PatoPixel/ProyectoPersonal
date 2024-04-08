import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AutenticacionService } from '../services/autenticacion.service';


@Injectable({
  providedIn: 'root'
})
export class AutenticacionGuard implements CanActivate {

  constructor(private autenticacionService: AutenticacionService, private router: Router) {}

  canActivate(): boolean {
    if (!this.autenticacionService.habilitarlogeo()) {
      this.router.navigate(['/login']);
      return false;
    }
    return true;
  }
}