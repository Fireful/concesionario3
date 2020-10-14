import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Concesionario3TestModule } from '../../../test.module';
import { CocheUpdateComponent } from 'app/entities/coche/coche-update.component';
import { CocheService } from 'app/entities/coche/coche.service';
import { Coche } from 'app/shared/model/coche.model';

describe('Component Tests', () => {
  describe('Coche Management Update Component', () => {
    let comp: CocheUpdateComponent;
    let fixture: ComponentFixture<CocheUpdateComponent>;
    let service: CocheService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Concesionario3TestModule],
        declarations: [CocheUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CocheUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CocheUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CocheService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Coche(123);
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
        const entity = new Coche();
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
