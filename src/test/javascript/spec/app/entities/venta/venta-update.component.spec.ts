import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Concesionario3TestModule } from '../../../test.module';
import { VentaUpdateComponent } from 'app/entities/venta/venta-update.component';
import { VentaService } from 'app/entities/venta/venta.service';
import { Venta } from 'app/shared/model/venta.model';

describe('Component Tests', () => {
  describe('Venta Management Update Component', () => {
    let comp: VentaUpdateComponent;
    let fixture: ComponentFixture<VentaUpdateComponent>;
    let service: VentaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Concesionario3TestModule],
        declarations: [VentaUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(VentaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VentaUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VentaService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Venta(123);
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
        const entity = new Venta();
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
