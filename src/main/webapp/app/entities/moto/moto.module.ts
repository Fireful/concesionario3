import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Concesionario3SharedModule } from 'app/shared/shared.module';
import { MotoComponent } from './moto.component';
import { MotoDetailComponent } from './moto-detail.component';
import { MotoUpdateComponent } from './moto-update.component';
import { MotoDeleteDialogComponent } from './moto-delete-dialog.component';
import { motoRoute } from './moto.route';
import { MotoVendidosComponent } from './moto-vendidos.component';

@NgModule({
  imports: [Concesionario3SharedModule, RouterModule.forChild(motoRoute)],
  declarations: [MotoComponent, MotoDetailComponent, MotoUpdateComponent, MotoDeleteDialogComponent, MotoVendidosComponent],
  entryComponents: [MotoDeleteDialogComponent]
})
export class Concesionario3MotoModule {}
