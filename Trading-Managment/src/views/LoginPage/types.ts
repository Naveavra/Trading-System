import { LoginPostData } from '../..//types/requestTypes/authTypes'

export interface LoginFormValues extends LoginPostData {
    rememberMe: boolean;
};