import AnswerQuestion from "../../components/Forms/AnswerQuestion";

export interface GetStoresParams {
    userId: number,
    storeId: number,
}

export interface PostStoreParams {
    userId: number,
    name: string,
    img: string,
    desc: string //description
}

export interface PatchStoreParams {
    userId: number,
    storeId: number,
    desc: string | null,
    isActive: boolean | null
    img: string,
    name: string,
}

export interface DeleteStoreParams {
    userId: number,
    storeId: number
}

export interface GetStoreProducts {
    storeId: number
}
export interface AppointUserParams {
    userId: number;
    storeId: number;
    emailOfUser: string;
}
export interface fireUserParams {
    userId: number;
    storeId: number;
    userToFire: number;
}
export interface getAppointmentsHistoryParams {
    storeId: number,
    userId: number
}
export interface patchPermissionsParams {
    userId: number,
    storeId: number,
    managerId: number,
    permissions: string[]
    mode: string;
}
export interface AnswerQuestionParams {
    userId: number;
    storeId: number;
    questionId: number;
    answer: string;
}
export interface WriteReviewParams {
    userId: number;
    storeName: string;
    rating: number;
    content: string;
};
export interface WriteQuestionParams {
    userId: number;
    storeId: number;
    question: string;
};
export interface WriteReviewOnProductParams {
    userId: number;
    storeId: number;
    productId: number;
    rating: number;
    content: string;
};