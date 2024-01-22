export interface TransactionRecord {
    id: Number,
    tId: string,
    type: string,
    sId: string,
    dId: string,
    amount: Number,
    date: Date,
    status: string,
    sBalance: Number,
    dBalance: Number
}