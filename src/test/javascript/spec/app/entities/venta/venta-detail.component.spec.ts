import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Concesionario3TestModule } from '../../../test.module';
import { VentaDetailComponent } from 'app/entities/venta/venta-detail.component';
import { Venta } from 'app/shared/model/venta.model';

describe('Component Tests', () => {
  describe('Venta Management Detail Component', () => {
    let comp: VentaDetailComponent;
    let fixture: ComponentFixture<VentaDetailComponent>;
    const route = ({ data: of({ venta: new Venta(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Concesionario3TestModule],
        declarations: [VentaDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(VentaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VentaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load venta on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.venta).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
