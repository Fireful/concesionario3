import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IMoto, Moto } from 'app/shared/model/moto.model';
import { MotoService } from './moto.service';

@Component({
  selector: 'jhi-moto-update',
  templateUrl: './moto-update.component.html'
})
export class MotoUpdateComponent implements OnInit {
  isSaving = false;
  texto: any;
  colorinput: any;
  editForm = this.fb.group({
    id: [],
    marca: [],
    modelo: [],
    anio: [],
    electrico: [],
    color: [],
    cilindrada: [],
    precio: []
  });

  constructor(protected motoService: MotoService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  public color(colorForm: string): void {
    const inputElement = document.getElementById('color') as HTMLInputElement;
    inputElement.value = colorForm;
    inputElement.style.backgroundColor = colorForm;
    this.editForm.controls.color.setValue(colorForm);
    this.editForm.controls.backcolor.setValue(colorForm);
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ moto }) => {
      this.updateForm(moto);
    });
  }

  updateForm(moto: IMoto): void {
    this.editForm.patchValue({
      id: moto.id,
      marca: moto.marca,
      modelo: moto.modelo,
      anio: moto.anio,
      electrico: moto.electrico,
      precio: moto.precio,
      color: moto.color
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const moto = this.createFromForm();

    if (moto.id !== undefined) {
      this.subscribeToSaveResponse(this.motoService.update(moto));
    } else {
      this.subscribeToSaveResponse(this.motoService.create(moto));
    }
  }

  private createFromForm(): IMoto {
    return {
      ...new Moto(),
      id: this.editForm.get(['id'])!.value,
      marca: this.editForm.get(['marca'])!.value,
      modelo: this.editForm.get(['modelo'])!.value,
      anio: this.editForm.get(['anio'])!.value,
      electrico: this.editForm.get(['electrico'])!.value,
      precio: this.editForm.get(['precio'])!.value,
      color: this.editForm.get(['color'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMoto>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
