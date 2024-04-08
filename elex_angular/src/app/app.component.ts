import { Component } from '@angular/core';
import { AutenticacionService } from './services/autenticacion.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'elex_angular';
 

  constructor(private loginPrd:AutenticacionService) {
    
  }
  public visualizarMenu():boolean{
    return this.loginPrd.habilitarlogeo()
  }

}
