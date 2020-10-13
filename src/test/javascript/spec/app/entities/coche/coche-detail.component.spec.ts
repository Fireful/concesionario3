import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Concesionario3TestModule } from '../../../test.module';
import { CocheDetailComponent } from 'app/entities/coche/coche-detail.component';
import { Coche } from 'app/shared/model/coche.model';

describe('Component Tests', () => {
  describe('Coche Management Detail Component', () => {
    let comp: CocheDetailComponent;
    let fixture: ComponentFixture<CocheDetailComponent>;
    const route = ({ data: of({ coche: new Coche(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Concesionario3TestModule],
        declarations: [CocheDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CocheDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CocheDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load coche on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.coche).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
