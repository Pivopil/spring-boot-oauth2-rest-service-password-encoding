import {Pipe, PipeTransform} from '@angular/core';

@Pipe({name: 'translate'})
export class TranslatePipe implements PipeTransform {


    data:any = {
        'en_US': {

            // home view
            HOME: 'Home',
            SIGN_IN: 'Sign in',
            SIGN_OUT: 'Sign Out',
            HI: 'Hi, ',

            // info view
            CONSUMPTION_DASHBOARD: 'Angular 2 Spring Boot OAuth2 Client',
            PROJECT_DESCRIPTION: 'Project description',

            // admin view
            ADMIN_PANEL: 'Admin panel',
            SELECT_CUSTOMER: 'Select customer: ',
            SHOW_DATA: 'Show Data...',
            NEW: 'New...',
            CONFIGURE_DEVICE: 'Configure device: ',
            CONFIGURE: 'Configure...',

        }
    };

    transform(value:any, args:string):string {
        let language = 'en_US';
        if (args === 'en_US') {
            language = args;
        } else if (localStorage.getItem('language') === 'en_US') {
            language = localStorage.getItem('language');
        }
        return this.data[language][value];
    }
}