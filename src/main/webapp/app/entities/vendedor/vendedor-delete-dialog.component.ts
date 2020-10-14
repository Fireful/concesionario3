import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVendedor } from 'app/shared/model/vendedor.model';
import { VendedorService } from './vendedor.service';

@Component({
  templateUrl: './vendedor-delete-dialog.component.html'
})
export class VendedorDeleteDialogComponent {
  vendedor?: IVendedor;

  constructor(protected vendedorService: VendedorService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vendedorService.delete(id).subscribe(() => {
      this.eventManager.broadcast('vendedorListModification');
      this.activeModal.close();
    });
  }
}
