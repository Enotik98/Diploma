import moment from "moment/moment";
import {sendRequest} from "@/scripts/request.js";
export function formatDate(val){
    return (val) ? moment(val).format("DD/MM/YYYY"): ""
}

export async function getGenreList() {
    return await sendRequest("/book/genre", "GET", null);
}