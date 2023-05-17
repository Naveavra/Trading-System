import { KeyboardEvent, KeyboardEventHandler, useState } from "react";
import SearchIcon from "@mui/icons-material/Search";

import './SearchBar.css';
interface props {
    text: string;
    set: (s: string) => void;
}
export const SearchBar: React.FC<props> = ({ text, set }) => {
    const handleChange = (value: string) => {
        //set(value);
    };
    const handleSubmit = (value: string) => {
        set(value);
    };

    return (
        <div className="input-wrapper">
            <SearchIcon id="search-icon" />
            <input
                placeholder="Type to search..."
                value={text}
                onChange={(e) => handleChange(e.target.value)}
                onKeyDown={(event: KeyboardEvent<HTMLInputElement>) => {
                    if (event.keyCode === 13) { // 13 is the code for 'Enter' key
                        handleSubmit(event.currentTarget.value);
                    }
                }}
            />
        </div>
    );
};