import { Component, OnInit, ViewChild } from '@angular/core';
import { Tiposexpediente } from '../../models/tiposexpediente.model';
import { TiposexpedienteService } from '../../services/tiposexpediente.service';
import { HttpErrorResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';
@Component({
  selector: 'app-formularios-tipos',
  //standalone: true,
  templateUrl: './formularios-tipos.component.html',
  styleUrl: './formularios-tipos.component.css'
})
export class FormulariosTiposComponent implements OnInit{
  constructor (private TiposExpedienteService: TiposexpedienteService) {}

  tipos: Tiposexpediente[] = [];
  tipoBuscar: Tiposexpediente[] = [];


  // Propiedades para el formulario
  materia = "";
  activo = 1;
  
  // Propiedades extras

  mensaje:string = ""
  mensajeError:string = ""
  materiaSeleccionada = ""
  materiaNueva = "";
  anadir = false;
  materiaConsultar = "";
  activoNuevo = 0;

  alternarAI = 1
  mostrarActivosInactivos = 1
  mensajeActivos = "Activos"
  @ViewChild('form', { static: false }) form: NgForm | undefined;
  

  //Al arrancar la aplicacion
  ngOnInit(): void {
      this.cargarTipos();
  }

  // Funcionalidades

  editarTipo(materia: string): void {
    this.materiaSeleccionada = materia;
    this.materiaNueva = materia;
  }

  cancelarTipo(): void {
    this.materiaSeleccionada = "";
  }

  anadirFormulario(valor: boolean): void{
    this.anadir = valor
  }

  alternarActivoInactivo():void{
    if (this.alternarAI == 1){
      this.alternarAI = 0;
      this.mostrarActivosInactivos = 0;
    }else{
      this.alternarAI = 1;
      this.mostrarActivosInactivos = 1;
    }
    this.cambiarMensajeActivos()

  }
  mostrarAI():void{
    this.mostrarActivosInactivos = this.alternarAI === 1 ? 0 : 1;
    this.cambiarMensajeActivos()
  }

  cambiarMensajeActivos():void{

    if(this.alternarAI == 1 && this.mostrarActivosInactivos == 1){
      this.mensajeActivos = "Activos"
    }else if (this.alternarAI == 0 && this.mostrarActivosInactivos == 0){
      this.mensajeActivos = "Inactivos"
    } else if (this.alternarAI == 1 && this.mostrarActivosInactivos == 0 || this.alternarAI == 0 && this.mostrarActivosInactivos == 1){
      this.mensajeActivos = "Activos e Inactivos"
    }
  }

  //CRUD

  cargarTipos(): void {
    this.TiposExpedienteService.consultarTipos().subscribe(data => {
      this.tipos = data;
      this.tipoBuscar = data;
    })
    this.cambiarMensajeActivos()
  }

  insertarTiposExpediente(): void {

    if (this.activo)this.activo = 1;
    else this.activo = 0;
    let nuevoTipo: Tiposexpediente = {
      materia: this.materia,
      activo: this.activo,
      materianueva: ''
    }
    this.TiposExpedienteService.insertarTipos(nuevoTipo).subscribe(result => {
      if (result) {
        this.mensaje = "Tipo de Expediente Insertado"
        this.mensajeError = '';
        this.cargarTipos();
      }
    },
    error => {
      console.error('Error al añadir tipos:', error);
      if (error instanceof HttpErrorResponse) {
        this.mensajeError = error.error; // Asignar el mensaje de error al atributo errorMsg
        this.mensaje = "";
      } else {
        this.mensajeError = 'Error desconocido'; // Manejar otros tipos de errorç
        this.mensaje = "";
      }
    }
  );
  if (this.form) {
    this.form.resetForm(); // Restablecer el formulario a su estado inicial
  }
    this.anadir = false;
    this.activo = 1;
  }

  cargarTipos2(materia: string): void {
    
    this.TiposExpedienteService.consultarTiposMateria(materia).subscribe(data => {
      if (Array.isArray(data)) {
        this.tipos = data;
      } else {
        this.tipos = [data]; // Convertir el objeto a una matriz de un solo elemento
      }
    })
    this.mostrarAI()
    this.mensajeActivos=""
  }
  
  actualizarTiposExpediente(materia: string,materiaNueva: string, activo: number): void {
    this.materiaSeleccionada = ""
    let nuevoTipo: Tiposexpediente = {
      materia: materia,
      activo: activo,
      materianueva: materiaNueva
    }
    this.TiposExpedienteService.actualizarTipos(nuevoTipo).subscribe(result => {
        if (result) {
          if (materia == materiaNueva)this.mensaje = ""
          else this.mensaje = "Tipo Expediente: '" + materia + "' se actualizó a: '" + materiaNueva + "'"
          
          this.mensajeError = ""
          this.cargarTipos();
        }
      },
      error => {
        console.error('Error al actualizar tipos:', error);
        if (error instanceof HttpErrorResponse) {
          this.mensajeError = error.error; // Asignar el mensaje de error al atributo errorMsg
          this.mensaje = "";
        } else {
          this.mensajeError = 'Error desconocido'; // Manejar otros tipos de errorç
          this.mensaje = "";
        }
      }
    );
  }
  activar(materia: string): void {
    let nuevoTipo: Tiposexpediente = {
      materia: materia,
      activo: 1,
      materianueva: materia
    }
    this.TiposExpedienteService.actualizarTipos(nuevoTipo).subscribe(result => {
        if (result) {
          this.mensaje = "Tipo Expediente: '" + materia + "' se activó"
          this.mensajeError = ""
          this.cargarTipos();
        }
      },
      (error) => {
        console.error('Error al actualizar tipos:', error);
      }
    );
  }

  borrarLogicamenteTiposExpediente(materia: string){
    this.TiposExpedienteService.borrarLogicamenteTipos(materia).subscribe(result => {
      if (result) {
        this.mensaje = "Tipo Expediente: '" + materia + "' se desactivó"
        this.mensajeError = ""
        this.cargarTipos();
      }
    },
    (error) => {
      console.error('Error al desactivar tipos:', error);
    }
  );
  }
  borrarFisicamenteTiposExpediente(materia: string){
    this.TiposExpedienteService.borrarFisicamenteTipos(materia).subscribe(result => {
      if (result) {
        this.mensaje = "Tipo Expediente: '" + materia + "' se eliminó"
        this.mensajeError = ""
        this.cargarTipos();
      }
    },
    (error) => {
      console.error('Tipo Eliminado Correctamente:', error);
      this.mensaje = "Tipo Expediente: '" + materia + "' se eliminó"
        this.mensajeError = ""
        this.cargarTipos();
    }
  );
  }
}