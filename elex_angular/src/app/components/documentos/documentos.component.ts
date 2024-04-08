import { DocumentosService } from '../../services/documentos.service';
import { Component, ViewChild } from '@angular/core';
import { ExpedientesService } from '../../services/expedientes.service';
import { Expedientes } from '../../models/expedientes.model';
import { HttpErrorResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';
import { Documentos } from '../../models/documentos.model';
import { MediaService } from '../../services/media.service';

@Component({
  selector: 'app-documentos',
  templateUrl: './documentos.component.html',
  styleUrl: './documentos.component.css'
})
export class DocumentosComponent {
  constructor (private ExpedientesService: ExpedientesService, private DocumentosService: DocumentosService
    ,private mediaService: MediaService
  ) {}

  url?: string

  tipos: Documentos[] = [];
  tipoBuscar: Documentos[] = [];

  // Propiedades para el formulario
  codigo = ""
  codigoNuevo = "";
  codigoSeleccionado = ""

  finalizado: number = 0;

  tarifa: number = 0.00;
  categoria: string = ""
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
  

  archivos: any[] = []
  archivoSeleccionado = ""
  archivoActu?: FormData
  archvivoactu2 = ""

  foto?: any
  //Al arrancar la aplicacion
  ngOnInit(): void {
      this.cargarTipos();
      this.BuscarExpedientes()
  }

  // Funcionalidades

  
  editarTipo(codigo: string, tarfia:number, modalidad: string, materia:string ): void {
    this.codigoSeleccionado = codigo;
    this.codigoNuevo = codigo;
    this.tarifa = tarfia
    this.categoria = modalidad
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
    this.codigoNuevo = "";
    this.tarifa = 0
    this.categoria = ""
    this.expedienteNuevo = ""
    this.expedienteSeleccionado = ""
    this.archvivoactu2 =""
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
    this.DocumentosService.consultarDocumentos().subscribe(data => {
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


    let nuevoTipo: Documentos = {
      ruta: this.codigo.replace(/\//g, "_"),
      rutaNueva: this.codigo.replace(/\//g, "_"),
      tarifa: this.tarifa,
      categoria: this.categoria,
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
    
    this.DocumentosService.insertarDocumentos(nuevoTipo, this.expedienteAnadir).subscribe(result => {
      if (result) {
        this.mensaje = "Documento Insertado"
        this.mensajeError = '';
        this.cargarTipos();
      }
    },
    error => {
      console.error('Error al añadir documento:', error);
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


  cargarTipos2(valor: string): void {
    
    this.DocumentosService.consultarDocumentosCodigo(valor.replace(/\//g, "_")).subscribe(data => {
      if (Array.isArray(data)) {
        this.tipos = data;
        this.mensajeError = ""
      } else {
        this.tipos = [data]; // Convertir el objeto a una matriz de un solo elemento
        this.mensajeError = ""
      }
    },error => {
      console.error('Error al actualizar documento:', error);
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
  
  actualizarTiposExpediente(codigo: string,codigoNuevo: string,  finalizado: number, modalidad: string, tiposexpediente:Expedientes, activo: number, formData: FormData): void {
    this.codigoSeleccionado = ""
    let nuevoTipo: Documentos = {
      ruta: codigo.replace(/\//g, "_"),
      rutaNueva: codigoNuevo.replace(/\//g, "_"),
      tarifa: finalizado,
      categoria: modalidad,
      expediente: tiposexpediente,
      activo : activo
    }
    this.DocumentosService.actualizarDocumentos(nuevoTipo, this.expedienteNuevo).subscribe(result => {
        if (result) {
          if (codigo != codigoNuevo){
             this.mensaje = "Documento: '" + codigo + "' se actualizó a: '" + codigoNuevo + "'"
          }else{
            this.mensaje ="Documento: '" + codigo + "' se actualizó"
          }
         
          this.mensajeError = ""
          this.cargarTipos();
        }
      },
      error => {
        console.error('Error al actualizar documento:', error);
        if (error instanceof HttpErrorResponse) {
          this.mensajeError = error.error; // Asignar el mensaje de error al atributo errorMsg
          this.mensaje = "";
          this.mediaService.uploadFile(formData).subscribe()
          
        } else {
          this.mensajeError = 'Error desconocido'; // Manejar otros tipos de error
          this.mensaje = "";
            this.mediaService.uploadFile(formData).subscribe()
        }
      }
    );
  }

  activar(codigo: string): void {
    this.DocumentosService.activarDocumentos(codigo.replace(/\//g, "_")).subscribe(result => {
        if (result) {
          this.mensaje = "Documento: '" + codigo + "' se activó"
          this.mensajeError = ""
          this.cargarTipos();
        }
      },
      (error) => {
        console.error('Error al actualizar documento:', error);
      }
    );
  }

  borrarLogicamenteDocumentos(codigo: string){
    this.DocumentosService.borrarLogicamenteDocumentos(codigo.replace(/\//g, "_")).subscribe(result => {
      if (result) {
        this.mensaje = "Documento: '" + codigo + "' se desactivó"
        this.mensajeError = ""
        this.cargarTipos();
      }
    },
    (error) => {
      console.error('Error al desactivar documento:', error);
    }
  );
  }

  borrarFisicamenteDocumentos(codigo: string){
    if (confirm("¿Está seguro? Se eliminará el registro permanentemente")){
    this.DocumentosService.borrarFisicamenteDocumentos(codigo.replace(/\//g, "_")).subscribe(result => {
      if (result) {
        this.deleteFile(codigo.replace("/media/", ""));
        this.mensaje = "Documento: '" + codigo + "' se eliminó"
        this.mensajeError = ""
        this.cargarTipos();
      }
    },
    (error) => {
      this.deleteFile(codigo.replace("/media/", ""));
      console.error('Error al eleminar Documentos:', error);
      this.mensaje = "Actuacion: '" + codigo + "' se eliminó"
        this.mensajeError = ""
        this.cargarTipos();
    }
  );
}else{
  return
}

}


  //DOCUMENTO
  upload() {
    if (this.archivoSeleccionado) {
      let formData = new FormData();
      formData.append('file', this.archivoSeleccionado);

      this.mediaService.uploadFile(formData).subscribe(response => {
        console.log('response', response);
        let parsedUrl = new URL(response.url);
        this.url = response.url 
        this.codigo = parsedUrl.pathname;
        for (let i = 0; i < this.archivos.length; i++) {
          console.log(this.archivos.length);
        }
        this.insertarExpedientes()
      });
    }
  }

   uploadActualizacion(codigo: string,  finalizado: number, modalidad: string, tiposexpediente:Expedientes, activo: number) {
   if (this.archvivoactu2 == ""){
    
   let formData = this.uploadFile(codigo.replace("/media/", ""));
  
   
   this.actualizarTiposExpediente(codigo,this.codigoNuevo,finalizado,modalidad,tiposexpediente,activo, formData)

     }else{
       const formData = new FormData();
       formData.append('file', this.archvivoactu2);

       let formData2 = this.deleteFile(codigo.replace("/media/", ""));

     this.mediaService.uploadFile(formData).subscribe(response => {
       let parsedUrl = new URL(response.url);
        this.codigoNuevo = parsedUrl.pathname;

         this.actualizarTiposExpediente(codigo,this.codigoNuevo,finalizado,modalidad,tiposexpediente,activo, formData2)
        
       });
     }
   }

  uploadFile(archivo: any): FormData{
      this.mediaService.getFile(archivo).subscribe((data: Blob) => {

      const formData = new FormData();
      formData.append('file', data, archivo); // Aquí agregamos el blob al FormData

      this.mediaService.deleteFile(formData).subscribe(response => {
        console.log('Archivo cargado con éxito:', response);
        // Manejar la respuesta del servidor si es necesario
      }, error => {
        console.error('Error al cargar el archivo:', error);
      });

      this.mediaService.uploadFile(formData).subscribe(response => {
        console.log('Archivo cargado con éxito:', response);
        // Manejar la respuesta del servidor si es necesario
      }, error => {
        console.error('Error al cargar el archivo:', error);
      });
      return formData
    });
    return new FormData
  }
  deleteFile(archivo: any): FormData{
    this.mediaService.getFile(archivo).subscribe((data: Blob) => {

    const formData = new FormData();
    formData.append('file', data, archivo); // Aquí agregamos el blob al FormData

    this.mediaService.deleteFile(formData).subscribe(response => {
      console.log('Archivo cargado con éxito:', response);
      // Manejar la respuesta del servidor si es necesario
    }, error => {
      console.error('Error al cargar el archivo:', error);
    });
    return formData
  });
  return new FormData
}

  onFileSelected(event: any) {
    this.archivoSeleccionado = event.target.files[0];
  }

  onFileSelectedActu(event: any) {
    this.archivoActu = event.target.files[0];
    this.archvivoactu2 = event.target.files[0];
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
