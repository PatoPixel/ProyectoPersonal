import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MediaService {

  constructor(private http: HttpClient) { }

  uploadFile(FormData: FormData): Observable<any>{
    return this.http.post('http://localhost:8100/media/upload', FormData)
  }

  deleteFile(FormData: FormData): Observable<any>{
    return this.http.post('http://localhost:8100/media/delete', FormData)
  }

  getFile(file: string): Observable<any>
  {
    return this.http.get(`http://localhost:8100/media/${file}`, { responseType: 'blob' })
  }
  
}
