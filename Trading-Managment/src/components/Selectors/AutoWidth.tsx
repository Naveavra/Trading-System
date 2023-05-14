import * as React from 'react';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select, { SelectChangeEvent } from '@mui/material/Select';

interface SelectProps {
    label: string;
    values: string[];
    labels: string[];
    value: string;
    handleChange?: (event: SelectChangeEvent) => void;
}

const SelectAutoWidth: React.FC<SelectProps> = ({ label, values, labels, value, handleChange }) => {
    return (
        <div>
            <FormControl sx={{ minWidth: 90, mr: 1.5, width: '100%', mt: 2, mb: 2 }}>
                <InputLabel id="demo-simple-select-autowidth-label">{label}</InputLabel>
                <Select
                    labelId="demo-simple-select-autowidth-label"
                    id="demo-simple-select-autowidth"
                    value={value}
                    onChange={handleChange}
                    autoWidth
                    label={label}
                >
                    {values.map((value, index) => {
                        return <MenuItem key={index} value={value}>{labels[index]}</MenuItem>
                    })
                    }
                </Select>
            </FormControl>
        </div>
    );
}
export default SelectAutoWidth;