export const backendUrl = 'http://localhost:4567/';
export interface LocalStorageEntry<T> {
    name: string;
    value: T;
}

type Entry<T> = LocalStorageEntry<T>;
export interface LocalSorage {
    auth: {
        token: Entry<number | null>;
        userId: Entry<number | null>;
        userName: Entry<string | null>;
        isAdmin: Entry<boolean | null>;
    },
    settings: {
        login_page: {
            remember_me: Entry<boolean>;
        }
    }
};
export const localStorage: LocalSorage = {
    auth: {
        token: {
            name: 'auth.token',
            value: null,
        },
        userId: {
            name: 'auth.userId',
            value: null,
        },
        userName: {
            name: 'auth.userName',
            value: null,
        },
        isAdmin: {
            name: 'auth.isAdmin',
            value: null,
        },
    },
    settings: {
        login_page: {
            remember_me: {
                name: 'settings.login_page.remember_me',
                value: false,
            }
        }
    }
};
