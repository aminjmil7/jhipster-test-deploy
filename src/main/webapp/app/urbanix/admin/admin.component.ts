import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { UserManagementService } from 'app/admin/user-management/service/user-management.service';
import { IUser, User } from 'app/admin/user-management/user-management.model';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { TranslateService } from '@ngx-translate/core';
import { RegisterService } from 'app/account/register/register.service';

@Component({
  selector: 'jhi-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./style.scss'],
})
export class AdminComponent implements OnInit {
  users: User[] = [];
  usersSearch: IUser[] = [];
  sizes: number[] = [5, 10, 20, 50];
  currentSearch!: string;
  size = 20;
  selectedUser: IUser = {};
  currentUser!: Account;
  password = '';

  constructor(
    private userService: UserManagementService,
    protected accountService: AccountService,
    private translateService: TranslateService,
    private registerService: RegisterService
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe((account: Account | null) => {
      this.currentUser = account!;
      this.loadAll();
    });
  }

  loadAll(): void {
    this.selectedUser = {};
    this.userService.query({ size: this.size }).subscribe((res: HttpResponse<User[]>) => {
      this.users = res.body!.filter(user => user.authorities!.includes('ROLE_ADMIN'));
      this.usersSearch = res.body!.filter(user => user.authorities!.includes('ROLE_ADMIN'));
    });
  }

  search() {
    if (this.currentSearch) {
      this.users = this.usersSearch.filter(user => user.firstName?.toLocaleLowerCase().match(this.currentSearch.toLocaleLowerCase()));
    } else {
      this.users = this.usersSearch;
    }
  }

  save() {
    this.selectedUser.activated = this.selectedUser.activated ?? false;
    this.selectedUser.login = this.selectedUser.firstName!;
    if (this.selectedUser.id) {
      this.selectedUser.lastModifiedBy = this.currentUser.login;
      this.selectedUser.lastModifiedDate = new Date();
      this.userService.update(this.selectedUser).subscribe();
    } else {
      const login = this.selectedUser.login;
      const email = this.selectedUser.email!;
      const password = this.password;
      const firstName = this.selectedUser.firstName!;
      const lastName = this.selectedUser.lastName!;
      this.registerService
        .registerAdmin({
          login,
          email,
          password,
          langKey: this.translateService.currentLang,
          firstName,
          lastName,
        })
        .subscribe(() => this.loadAll());
    }
  }

  deleteUser(login: string) {
    this.userService.delete(login).subscribe(() => this.loadAll());
  }

  uploadFile(event: any): any {
    const reader = new FileReader();
    const file = event.target!.files[0];
    if (event.target!.files && event.target.files[0]) {
      reader.readAsDataURL(file);

      // When file uploads push it to file list
      reader.onload = () => {
        this.selectedUser.imageUrl = reader.result!.toString();
      };
    }
  }
}
