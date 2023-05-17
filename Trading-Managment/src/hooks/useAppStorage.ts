import { useState } from 'react';
import { useLocalStorage } from './useLocalStorage';
import { LocalStorageEntry } from '../config'

// Hook
export const useAppStorage = <T,>(entry: LocalStorageEntry<T>) => {
    return useLocalStorage(entry.name, entry.value);
}