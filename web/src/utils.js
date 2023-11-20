import {
    Box, Button,
    CardContent,
    CardHeader,
    Table, TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Typography
} from "@mui/material";
import Card from "@mui/material/Card";
import {Input} from '@mui/base/Input';
import React, {useState} from "react";
import { Link } from "react-router-dom";
import dayjs from "dayjs";




export function UpdateForm() {
    return (
        <div>
            <Box sx={{
                display: 'inline-flex',
                flexDirection: 'column',
            }}>
                <Card sx={{margin: 1, border: 5}}>
                    <CardHeader title="Ввод полей"/>
                    <CardContent>
                        <Typography component={'span'} variant={'body2'} align='left'>
                            <Input/>
                        </Typography>
                    </CardContent>
                </Card>
            </Box>
        </div>
    )
}