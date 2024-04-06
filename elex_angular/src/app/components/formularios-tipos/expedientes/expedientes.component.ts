import { Component } from '@angular/core';
import { ExpedientesService } from '../../../services/expedientes.service';
import { Expedientes } from '../../../models/expedientes.model';
import { TiposexpedienteService } from '../../../services/tiposexpediente.service';
import { Tiposexpediente } from '../../../models/tiposexpediente.model';
import { HttpErrorResponse } from '@angular/common/http';


@Component({
  selector: 'app-expedientes',
  templateUrl: './expedientes.component.html',
  styleUrl: './expedientes.component.css'
})
export class ExpedientesComponent {
  constructor (private ExpedientesService: ExpedientesService, private TiposExpedienteService: TiposexpedienteService) {}

  tipos: Expedientes[] = [];
  tipoBuscar: Expedientes[] = [];

  // Propiedades para el formulario
  codigo = ""
  fecha: Date = new Date()
  situacion: string = ""
  opciones: string = ""
  descripcion: string = ""
  prioridad: string = ""
  ubicacion: string = ""
  tipoExpdiente: Tiposexpediente = {
    materia: '',
    materianueva: '',
    activo: 1
  }
  activo = 1;
  
  // Propiedades extras

  mensaje:string = ""
  mensajeError:string = ""
  codigoSeleccionado = ""
  codigoNuevo = "";
  anadir = false;
  codigoConsultar = "";
  activoNuevo = 0;

  alternarAI = 1
  mostrarActivosInactivos = 1
  mensajeActivos = "Activos"
  
  

  //Al arrancar la aplicacion
  ngOnInit(): void {
      this.cargarTipos();
  }

  // Funcionalidades

  editarTipo(codigo: string, situacion: string, prioridad: string): void {
    this.codigoSeleccionado = codigo;
    this.codigoNuevo = codigo;
    this.situacion = situacion;  
    this.prioridad = prioridad
  }

  cancelarTipo(): void {
    this.codigoSeleccionado = "";
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
    this.ExpedientesService.consultarExpedientes().subscribe(data => {
      this.tipos = data;
      this.tipoBuscar = data;
    })
    this.cambiarMensajeActivos()
    console.log(this.tipos)
  }

  insertarExpedientes(): void {

    if (this.activo == 1)this.activo = 0;
    else this.activo = 1;

    let nuevoTipo: Expedientes = {
      codigo: this.codigo,
      codigoNuevo: this.codigo,
      fecha: this.fecha,
      situacion: this.situacion,
      opciones: "",
      descripcion: "",
      prioridad: "",
      ubicacion:"",
      tipoExpediente: {
        materia: '',
        materianueva: '',
        activo: 1
      },
      activo : 1
    }
    this.ExpedientesService.insertarExpedientes(nuevoTipo).subscribe(result => {
      if (result) {
        this.mensaje = "Expediente Insertado"
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
      
    this.anadir = false;
  }

  cargarTipos2(valor: string): void {
    
    this.ExpedientesService.consultarExpedientesCodigo(valor).subscribe(data => {
      if (Array.isArray(data)) {
        this.tipos = data;
      } else {
        this.tipos = [data]; // Convertir el objeto a una matriz de un solo elemento
      }
    })
    this.mostrarAI()
    this.mensajeActivos=""
    
  }
  
  actualizarTiposExpediente(
    codigo: string,codigoNuevo: string, fecha: Date, situacion: string, opciones: string, descripcion: string, prioridad:string,  ubicacion: string , activo: number
  ): void {
    let nuevoTipo: Expedientes = {
      codigo: codigo,
      codigoNuevo: codigoNuevo,
      fecha: fecha,
      situacion: situacion,
      opciones: opciones,
      descripcion: descripcion,
      prioridad: prioridad,
      ubicacion: ubicacion,
      tipoExpediente: {
        materia: '',
        materianueva: '',
        activo: 1
      },
      activo : activo
    }
    this.ExpedientesService.actualizarExpedientes(nuevoTipo).subscribe(result => {
        if (result) {
          this.mensaje = "Tipo Expediente: '" + codigo + "' se actualizó a: '" + codigoNuevo + "'"
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
  activar(codigo: string): void {
    let nuevoTipo: Expedientes = {
      codigo: codigo,
      activo: 1,
      codigoNuevo: codigo,
      fecha: this.fecha,
      situacion: this.situacion,
      opciones: this.opciones,
      descripcion: this.descripcion,
      prioridad: this.prioridad,
      ubicacion: this.ubicacion,
      tipoExpediente: this.tipoExpdiente
    }
    this.ExpedientesService.actualizarExpedientes(nuevoTipo).subscribe(result => {
        if (result) {
          this.mensaje = "Tipo Expediente: '" + codigo + "' se activó"
          this.mensajeError = ""
          this.cargarTipos();
        }
      },
      (error) => {
        console.error('Error al actualizar tipos:', error);
      }
    );
  }

  borrarLogicamenteExpedientes(codigo: string){
    this.ExpedientesService.borrarLogicamenteExpedientes(codigo).subscribe(result => {
      if (result) {
        this.mensaje = "Tipo Expediente: '" + codigo + "' se desactivó"
        this.mensajeError = ""
        this.cargarTipos();
      }
    },
    (error) => {
      console.error('Error al actualizar tipos:', error);
    }
  );
  }

  borrarFisicamenteExpedientes(codigo: string){
    this.ExpedientesService.borrarFisicamenteExpedientes(codigo).subscribe(result => {
      if (result) {
        this.mensaje = "Tipo Expediente: '" + codigo + "' se eliminó"
        this.mensajeError = ""
        this.cargarTipos();
      }
    },
    (error) => {
      console.error('Error al actualizar tipos:', error);
      this.mensaje = "Tipo Expediente: '" + codigo + "' se eliminó"
        this.mensajeError = ""
        this.cargarTipos();
    }
  );
  }
}
