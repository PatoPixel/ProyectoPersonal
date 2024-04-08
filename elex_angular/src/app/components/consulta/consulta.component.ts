import { ActuacionesService } from '../../services/actuaciones.service';
import { Component, ViewChild } from '@angular/core';
import { ExpedientesService } from '../../services/expedientes.service';
import { Expedientes } from '../../models/expedientes.model';
import { HttpErrorResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';
import { Actuaciones } from '../../models/actuaciones.model';
import { Documentos } from '../../models/documentos.model';
import { DocumentosService } from '../../services/documentos.service';
import { MediaService } from '../../services/media.service';
@Component({
  selector: 'app-consulta',
  templateUrl: './consulta.component.html',
  styleUrl: './consulta.component.css'
})
export class ConsultaComponent {

  constructor (private ExpedientesService: ExpedientesService, private ActuacionesService: ActuacionesService, private DocumentosService: DocumentosService,private mediaService: MediaService ) {}


  tipos: Actuaciones[] = [];
  tipoBuscar: Actuaciones[] = [];
  
  Documentos: Documentos[] = [];
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
  codigoConsultar = "";

  expedienteConsultar = "";
  expedienteNuevo = "";
  expedienteSeleccionado = ""
  expedienteAnadir = "";
  
  activoNuevo = 0;

  alternarAI = 1
  mostrarActivosInactivos = 1
  mensajeActivos = "Activos"

  foto = "";
  
 //Al arrancar la aplicacion
 ngOnInit(): void {
  this.cargarTipos();
  this.BuscarExpedientes()
  this.BuscarDocumentos()
}
BuscarExpedientes(): void {
    
  this.ExpedientesService.consultarExpedientes().subscribe(data => {
    if (Array.isArray(data)) {
      this.tipoExpdiente = data;
    } else {
      this.tipoExpdiente= [data]; // Convertir el objeto a una matriz de un solo elemento
    }
  })
}
BuscarDocumentos(): void {
    
  this.DocumentosService.consultarDocumentos().subscribe(data => {
    if (Array.isArray(data)) {
      this.Documentos = data;
    } else {
      this.Documentos = [data]; // Convertir el objeto a una matriz de un solo elemento
    }
  })
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
  cargarTipos(): void {
    this.ActuacionesService.consultarActuaciones().subscribe(data => {
      this.tipos = data;
      this.tipoBuscar = data;
    })
    this.cambiarMensajeActivos()
  }

  cargarTipos2(valor: string): void {
    
    this.ExpedientesService.consultarExpedientesCodigo(valor).subscribe(data => {
      if (Array.isArray(data)) {
        this.tipoExpedienteBuscar = data;
      } else {
        this.tipoExpedienteBuscar = [data]; // Convertir el objeto a una matriz de un solo elemento
      }
    })
    this.mostrarAI()
    this.mensajeActivos=""
    
  }
  verFoto(ruta: string): void {
    this.mediaService.getFile(ruta.replace("/media/", "")).subscribe(data => {


    const extension = ruta.split('.').pop()?.toLowerCase();
    if (extension === 'png' || extension === 'jpeg' || extension === 'jpg' || extension === 'gif' || extension === 'bmp' || extension === 'tiff' || extension === 'tif' || extension === 'webp' || extension === 'svg'){

      // Si no es un archivo PDF, asumimos que es un archivo de texto
      this.foto = URL.createObjectURL(data);
      
    }else{
      this.downloadFile(ruta)
    }
  }, error => {
    console.error('Error al cargar el archivo:', error);
  });
  }

  downloadFile(filename: string) {
    this.mediaService.getFile(filename.replace("/media/", "")).subscribe((data: Blob) => {
      const blob = new Blob([data], { type: 'application/octet-stream' });
      const url = window.URL.createObjectURL(blob);
      const anchor = document.createElement('a');
      anchor.download = filename;
      anchor.href = url;
      anchor.click();
      window.URL.revokeObjectURL(url);
    });
  }

  cerrarFoto(){
    this.foto = "";
  }
}
