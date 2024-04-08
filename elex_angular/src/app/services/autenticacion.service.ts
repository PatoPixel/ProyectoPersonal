import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AutenticacionService {

  private ingresar:boolean = false

  constructor() { }

  public ingreserAplicativo(obj:any):boolean{

    this.ingresar = obj.usuario == 'soltel' && obj.contraseña == 'admin'
    console.log(this.ingresar)  
    return this.ingresar
  }
  public habilitarlogeo(){
    return this.ingresar
  }
  
}
