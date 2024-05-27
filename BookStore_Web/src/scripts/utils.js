import moment from "moment/moment";
export function formatDate(val){
    return moment(val).format("DD/MM/YYYY")
}