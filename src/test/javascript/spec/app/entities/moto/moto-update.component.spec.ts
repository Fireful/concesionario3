import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Concesionario3TestModule } from '../../../test.module';
import { MotoUpdateComponent } from 'app/entities/moto/moto-update.component';
import { MotoService } from 'app/entities/moto/moto.service';
import { Moto } from 'app/shared/model/moto.model';

describe('Component Tests', () => {
  describe('Moto Management Update Component', () => {
    let comp: MotoUpdateComponent;
    let fixture: ComponentFixture<MotoUpdateComponent>;
    let service: MotoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Concesionario3TestModule],
        declarations: [MotoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MotoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MotoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MotoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Moto(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Moto();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
