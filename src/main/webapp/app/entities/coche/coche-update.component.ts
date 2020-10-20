import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICoche, Coche } from 'app/shared/model/coche.model';
import { CocheService } from './coche.service';

@Component({
  selector: 'jhi-coche-update',
  templateUrl: './coche-update.component.html'
})
export class CocheUpdateComponent implements OnInit {
  isSaving = false;
  texto: any;
  colorinput: any;
  editForm = this.fb.group({
    id: [],
    marca: [],
    modelo: [],
    anio: [],
    electrico: [],
    precio: [],
    color: []
  });

  constructor(protected cocheService: CocheService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  public color(colorForm: string): void {
    const inputElement = document.getElementById('color') as HTMLInputElement;
    inputElement.value = colorForm;
    this.editForm.controls.color.setValue(colorForm);
  }
  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ coche }) => {
      this.updateForm(coche);
    });
  }

  updateForm(coche: ICoche): void {
    this.editForm.patchValue({
      id: coche.id,
      marca: coche.marca,
      modelo: coche.modelo,
      anio: coche.anio,
      electrico: coche.electrico,
      precio: coche.precio,
      color: coche.color
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const coche = this.createFromForm();

    if (coche.id !== undefined) {
      this.subscribeToSaveResponse(this.cocheService.update(coche));
    } else {
      this.subscribeToSaveResponse(this.cocheService.create(coche));
    }
  }

  private createFromForm(): ICoche {
    return {
      ...new Coche(),
      id: this.editForm.get(['id'])!.value,
      marca: this.editForm.get(['marca'])!.value,
      modelo: this.editForm.get(['modelo'])!.value,
      anio: this.editForm.get(['anio'])!.value,
      electrico: this.editForm.get(['electrico'])!.value,
      precio: this.editForm.get(['precio'])!.value,
      color: this.editForm.get(['color'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICoche>>): void {
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
