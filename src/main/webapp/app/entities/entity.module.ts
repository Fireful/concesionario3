import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'coche',
        loadChildren: () => import('./coche/coche.module').then(m => m.Concesionario3CocheModule)
      },
      {
        path: 'vendedor',
        loadChildren: () => import('./vendedor/vendedor.module').then(m => m.Concesionario3VendedorModule)
      },
      {
        path: 'moto',
        loadChildren: () => import('./moto/moto.module').then(m => m.Concesionario3MotoModule)
      },
      {
        path: 'venta',
        loadChildren: () => import('./venta/venta.module').then(m => m.Concesionario3VentaModule)
      },
      {
        path: 'cliente',
        loadChildren: () => import('./cliente/cliente.module').then(m => m.Concesionario3ClienteModule)
      },
      {
        path: 'informes',
        loadChildren: () => import('./generar-informes/informes.module').then(m => m.InformesModule)
      }

      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class Concesionario3EntityModule {}
