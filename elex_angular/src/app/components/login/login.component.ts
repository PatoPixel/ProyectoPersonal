import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AutenticacionService } from '../../services/autenticacion.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  myForm!: FormGroup;
  
  constructor(private fb: FormBuilder, private loginPrd: AutenticacionService, private router: Router) { } // Inyecta Router

  ngOnInit(): void {
    this.myForm = this.createMyForm();
  }

  private createMyForm(): FormGroup {
    return this.fb.group({
      usuario: ['', Validators.required],
      contraseña: ['', Validators.required]
    });
  }

  submitFormulario(): void {
    if(this.myForm.invalid){
      Object.values(this.myForm.controls).forEach(control =>{
        control.markAllAsTouched();
      })
      return;
    }
    console.log(this.myForm.value.contraseña)
    if(!this.loginPrd.ingreserAplicativo(this.myForm.value))
      {
        alert("Usuario o contraseñas inválidos");
      }
      else {
        this.router.navigate(['/index']); // Redirige al usuario a la ruta '/index'
      }
  }

  public get f(): any{
    return this.myForm.controls;
  }
}

