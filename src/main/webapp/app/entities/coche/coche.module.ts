import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Concesionario3SharedModule } from 'app/shared/shared.module';
import { CocheComponent } from './coche.component';
import { CocheDetailComponent } from './coche-detail.component';
import { CocheUpdateComponent } from './coche-update.component';
import { CocheDeleteDialogComponent } from './coche-delete-dialog.component';
import { cocheRoute } from './coche.route';
import { CocheVendidosComponent } from './coche-vendidos.component';
import { CocheDisponiblesComponent } from './coche-disponibles.component';

@NgModule({
  imports: [Concesionario3SharedModule, RouterModule.forChild(cocheRoute)],
  declarations: [
    CocheComponent,
    CocheDetailComponent,
    CocheUpdateComponent,
    CocheDeleteDialogComponent,
    CocheDisponiblesComponent,
    CocheVendidosComponent
  ],
  entryComponents: [CocheDeleteDialogComponent]
})
export class Concesionario3CocheModule {}
