

export const frontPort = 5173;
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
        notifications: Entry<string[] | null>;
        hasQestions: Entry<boolean | null>;
        storeRoles: Entry<string[] | null>;
        age: Entry<number | null>;
        birthday: Entry<string | null>;
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
        notifications: {
            name: 'auth.notifications',
            value: null,
        },
        hasQestions: {
            name: 'auth.hasQestions',
            value: null,
        },
        storeRoles: {
            name: 'auth.storeRoles',
            value: null,
        },
        age: {
            name: 'auth.age',
            value: null,
        },
        birthday: {
            name: 'auth.birthday',
            value: null,
        }
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
