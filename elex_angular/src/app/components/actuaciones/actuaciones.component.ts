import { ActuacionesService } from '../../services/actuaciones.service';
import { Component, ViewChild } from '@angular/core';
import { ExpedientesService } from '../../services/expedientes.service';
import { Expedientes } from '../../models/expedientes.model';
import { HttpErrorResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';
import { Actuaciones } from '../../models/actuaciones.model';

@Component({
  selector: 'app-actuaciones',
  templateUrl: './actuaciones.component.html',
  styleUrl: './actuaciones.component.css'
})
export class ActuacionesComponent {
  constructor (private ExpedientesService: ExpedientesService, private ActuacionesService: ActuacionesService) {}

  tipos: Actuaciones[] = [];
  tipoBuscar: Actuaciones[] = [];
  

  // Propiedades para el formulario
  codigo = ""
  codigoNuevo = "";
  codigoSeleccionado = ""

  finalizado: number = 0;

  fecha: Date = new Date;
  modalidad: string = ""
  tipoExpdiente: Expedientes[] = []
  activo:number = 1;
  
  // Propiedades extras
  @ViewChild('form', { static: false }) form: NgForm | undefined;
  tipoExpedienteBuscar: Expedientes[] = []
  mensaje:string = ""
  mensajeError:string = ""
  
  
  anadir = false;


  codigoConsultar = "";

  expedienteConsultar = "";
  expedienteNuevo = "";
  expedienteSeleccionado = ""
  expedienteAnadir = "";
  
  activoNuevo = 0;

  alternarAI = 1
  mostrarActivosInactivos = 1
  mensajeActivos = "Activos"
  
  

  //Al arrancar la aplicacion
  ngOnInit(): void {
      this.cargarTipos();
      this.BuscarExpedientes()
  }

  // Funcionalidades

  editarTipo(codigo: string, finalizado:number, fecha: Date , modalidad: string, materia:string ): void {
    this.codigoSeleccionado = codigo;
    this.codigoNuevo = codigo;
    this.fecha = fecha
    this.finalizado = finalizado
    this.modalidad = modalidad
    this.expedienteNuevo = materia
    this.expedienteSeleccionado = materia
  }

  BuscarExpedientes(): void {
    
    this.ExpedientesService.consultarExpedientes().subscribe(data => {
      if (Array.isArray(data)) {
        this.tipoExpedienteBuscar = data;
      } else {
        this.tipoExpedienteBuscar = [data]; // Convertir el objeto a una matriz de un solo elemento
      }
    })
  }

  cancelarTipo(): void {
    this.codigoSeleccionado = "";
  }

  anadirFormulario(valor: boolean): void{
    this.anadir = valor
    this.BuscarExpedientes()
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
  formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2); // Agrega un 0 al principio si el mes es < 10
    const day = ('0' + date.getDate()).slice(-2); // Agrega un 0 al principio si el día es < 10
    return `${year}-${month}-${day}`;
  }

  //CRUD

  cargarTipos(): void {
    this.ActuacionesService.consultarActuaciones().subscribe(data => {
      this.tipos = data;
      this.tipoBuscar = data;
    })
    this.cambiarMensajeActivos()
  }

  insertarExpedientes(): void {
    if (this.finalizado)this.finalizado = 1
      else this.finalizado = 0    

      if (this.activo)this.activo = 1
      else this.activo = 0  


    let nuevoTipo: Actuaciones = {
      descripcion: this.codigo,
      descripcionNueva: this.codigo,
      fecha: this.fecha,
      finalizado: this.finalizado,
      modalidad: this.modalidad,
      expediente: {
        codigo: '',
        codigoNuevo: '',
        fecha: new Date,
        situacion: '',
        opciones: '',
        descripcion: '',
        prioridad: '',
        ubicacion: '',
        tipoExpediente: {
          materia: '',
          materianueva: '',
          activo: 0
        },
        activo: 0
      },
      activo: this.activo,
      
    }
    
    this.ActuacionesService.insertarActuaciones(nuevoTipo, this.expedienteAnadir).subscribe(result => {
      if (result) {
        this.mensaje = "Actuacion Insertada"
        this.mensajeError = '';
        this.cargarTipos();
      }
    },
    error => {
      console.error('Error al añadir actuacion:', error);
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
  

  cargarTipos2(valor: string, fecha: string): void {
    
    this.ActuacionesService.consultarActacionesDescripcionYFecha(valor, fecha).subscribe(data => {
      if (Array.isArray(data)) {
        this.tipos = data;
        this.mensajeError = ""
      } else {
        this.tipos = [data]; // Convertir el objeto a una matriz de un solo elemento
        this.mensajeError = ""
      }
    },error => {
      console.error('Error al actualizar tipos:', error);
      if (error instanceof HttpErrorResponse) {
        this.mensajeError = error.error; // Asignar el mensaje de error al atributo errorMsg
        this.mensaje = "";
      } else {
        this.mensajeError = ''; // Manejar otros tipos de error
        this.mensaje = "";
      }
    }
  )
    this.mostrarAI()
    this.mensajeActivos=""
    
  }
  
  actualizarTiposExpediente(
    codigo: string,codigoNuevo: string,  finalizado: number, fecha: Date,modalidad: string, tiposexpediente:Expedientes, activo: number
  ): void {
    this.codigoSeleccionado = ""

      if (finalizado)finalizado = 1
      else finalizado = 0
    let nuevoTipo: Actuaciones = {
      descripcion: codigo,
      descripcionNueva: codigoNuevo,
      finalizado: finalizado,
      fecha: fecha,
      modalidad: modalidad,
      expediente: tiposexpediente,
      activo : activo
    }
  
    console.log(activo)
    this.ActuacionesService.actualizarActuaciones(nuevoTipo, this.expedienteSeleccionado ,this.expedienteNuevo).subscribe(result => {
        if (result) {
          if (codigo != codigoNuevo){
             this.mensaje = "Actuación: '" + codigo + "' se actualizó a: '" + codigoNuevo + "'"
          }else{
            this.mensaje ="Actuación: '" + codigo + "' se actualizó"
          }
         
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
          this.mensajeError = 'Error desconocido'; // Manejar otros tipos de error
          this.mensaje = "";
        }
      }
    );
  }

  activar(codigo: string, expediente: string): void {
    this.ActuacionesService.activarActuaciones(codigo, expediente).subscribe(result => {
        if (result) {
          this.mensaje = "Actuacion: '" + codigo + "' en expediente: '" + expediente+ "' se activó"
          this.mensajeError = ""
          this.cargarTipos();
        }
      },
      (error) => {
        console.error('Error al actualizar tipos:', error);
      }
    );
  }

  borrarLogicamenteActuaciones(codigo: string, expediente: string){
    this.ActuacionesService.borrarLogicamenteActuaciones(codigo, expediente).subscribe(result => {
      if (result) {
        this.mensaje = "Actuacion: '" + codigo + "' en expediente: '" + expediente+ "' se desactivó"
        this.mensajeError = ""
        this.cargarTipos();
      }
    },
    (error) => {
      console.error('Error al desactivar tipos:', error);
    }
  );
  }

  borrarFisicamenteActuaciones(codigo: string, expediente: string){
    if (confirm("¿Está seguro? Se eliminará el registro permanentemente")){
    this.ActuacionesService.borrarFisicamenteActuaciones(codigo, expediente).subscribe(result => {
      if (result) {
        this.mensaje = "Actuacion: '" + codigo + "' se eliminó"
        this.mensajeError = ""
        this.cargarTipos();
      }
    },
    (error) => {
      console.error('Error al eleminar actuaciones:', error);
      this.mensaje = "Actuacion: '" + codigo + "' en expediente: '" + expediente+ "' se eliminó"
        this.mensajeError = ""
        this.cargarTipos();
    }
  );
  }}
}

