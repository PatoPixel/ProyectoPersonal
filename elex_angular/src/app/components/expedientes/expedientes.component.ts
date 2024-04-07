import { Component, ViewChild } from '@angular/core';
import { ExpedientesService } from '../../services/expedientes.service';
import { Expedientes } from '../../models/expedientes.model';
import { TiposexpedienteService } from '../../services/tiposexpediente.service';
import { Tiposexpediente } from '../../models/tiposexpediente.model';
import { HttpErrorResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';


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
  fecha: Date = new Date;
  situacion: string = ""
  opciones: string = ""
  descripcion: string = ""
  prioridad: string = ""
  ubicacion: string = ""
  tipoExpdiente: Tiposexpediente[] = []
  activo:number = 1;
  
  // Propiedades extras
  @ViewChild('form', { static: false }) form: NgForm | undefined;
  tipoExpedienteBuscar: Tiposexpediente[] = []
  mensaje:string = ""
  mensajeError:string = ""
  codigoSeleccionado = ""
  codigoNuevo = "";
  anadir = false;
  codigoConsultar = "";
  materiaNueva = "";
  materiaAnadir = "Judicial";
  activoNuevo = 0;

  alternarAI = 1
  mostrarActivosInactivos = 1
  mensajeActivos = "Activos"
  
  

  //Al arrancar la aplicacion
  ngOnInit(): void {
      this.cargarTipos();
  }

  // Funcionalidades

  editarTipo(codigo: string, fecha: Date ,situacion: string,opciones: string, descripcion: string, prioridad: string, Ubicacion: string, materia:string ): void {
    this.codigoSeleccionado = codigo;
    this.codigoNuevo = codigo;
    this.fecha = fecha
    this.situacion = situacion;  
    this.prioridad = prioridad
    this.opciones = opciones
    this.descripcion = descripcion
    this.ubicacion = Ubicacion.replace(/_/g, "/")
    this.BuscarTiposExpediente()
    this.materiaNueva = materia
  }

  BuscarTiposExpediente(): void {
    
    this.TiposExpedienteService.consultarTipos().subscribe(data => {
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
    this.BuscarTiposExpediente()
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
    this.ExpedientesService.consultarExpedientes().subscribe(data => {
      this.tipos = data;
      this.tipoBuscar = data;
      this.tipos.forEach(tipo => {
        tipo.ubicacion = tipo.ubicacion.replace(/_/g, '/');
        tipo.situacion = tipo.situacion.replace(/_/g, ' ')
      });
    })
    this.cambiarMensajeActivos()
  }

  insertarExpedientes(): void {

    if (this.activo == 1)this.activo = 0;
    else this.activo = 1;
    let nuevoTipo: Expedientes = {
      codigo: this.codigo,
      codigoNuevo: this.codigo,
      fecha: this.fecha,
      situacion: this.situacion,
      opciones: this.opciones,
      descripcion: this.descripcion,
      prioridad: this.prioridad,
      ubicacion:this.ubicacion,
      
      tipoExpediente: {
        materia: '',
        materianueva: '',
        activo: 1
      },
      activo : this.activo
    }
    
    this.ExpedientesService.insertarExpedientes(nuevoTipo, this.materiaAnadir).subscribe(result => {
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
      if (this.form) {
      this.form.resetForm(); // Restablecer el formulario a su estado inicial
    }
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
    codigo: string,codigoNuevo: string, fecha: Date, situacion: string, opciones: string, descripcion: string, prioridad:string,  ubicacion: string, tiposexpediente:Tiposexpediente, activo: number
  ): void {
    this.codigoSeleccionado = ""
      
    let nuevoTipo: Expedientes = {
      codigo: codigo,
      codigoNuevo: codigoNuevo,
      fecha: fecha,
      situacion: situacion,
      opciones: opciones,
      descripcion: descripcion,
      prioridad: prioridad,
      ubicacion: ubicacion = ubicacion.replace(/\//g, "_"),
      tipoExpediente: tiposexpediente,
      activo : activo
    }
    this.ExpedientesService.actualizarExpedientes(nuevoTipo, this.materiaNueva ).subscribe(result => {
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
          this.mensajeError = 'Error desconocido'; // Manejar otros tipos de error
          this.mensaje = "";
        }
      }
    );
  }

  activar(codigo: string): void {
    this.ExpedientesService.activarExpedientes(codigo).subscribe(result => {
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
