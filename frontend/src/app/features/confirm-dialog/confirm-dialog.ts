import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import {CurrencyPipe} from '@angular/common';

@Component({
  standalone: true,
  imports: [MatDialogModule, MatButtonModule, CurrencyPipe],
  template: `
    <h2 mat-dialog-title>Confirm Order</h2>
    <mat-dialog-content>
      Do you really want to place the order for <strong>{{data.total | currency:'EUR'}}</strong>?
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button (click)="ref.close(false)">Cancel</button>
      <button mat-flat-button color="primary" (click)="ref.close(true)">Place Order</button>
    </mat-dialog-actions>
  `
})
export class ConfirmDialog {
  constructor(
    public ref: MatDialogRef<ConfirmDialog>,
    @Inject(MAT_DIALOG_DATA) public data: { total: number }
  ) {}
}
