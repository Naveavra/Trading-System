import { KeyboardEvent, KeyboardEventHandler, useState } from "react";
import SearchIcon from "@mui/icons-material/Search";

import './SearchBar.css';

export const SearchBar = () => {
    const [input, setInput] = useState("");

    //   const fetchData = (value) => {
    //     fetch("https://jsonplaceholder.typicode.com/users")
    //       .then((response) => response.json())
    //       .then((json) => {
    //         const results = json.filter((user) => {
    //           return (
    //             value &&
    //             user &&
    //             user.name &&
    //             user.name.toLowerCase().includes(value)
    //           );
    //         });
    //         setResults(results);
    //       });
    //   };

    const handleChange = (value: string) => {

        setInput(value);
    };
    const handleSubmit = (value: string) => {
        console.log(value);

        //fetchData(value);
    };

    return (
        <div className="input-wrapper">
            <SearchIcon id="search-icon" />
            <input
                placeholder="Type to search..."
                value={input}
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