export type Envelope = {
    envelopeId: number;
    transactionId: number;
    status: 'COMPLETED' | 'PENDING' | string;
    signedAt: string;
    signers: Signer[];
}

export type Signer = {
    signerId: number;
    envelopeId: number;
    envelopeStatus: string;
    signerEmail: string;
    signedAt: string | null;
    userId: number;
}

export type Transaction = {
    transactionID: number;
    amount: number;
    timestamp: string | null;
    senderAccountId: number;
    receiverAccountId: number;
    status: "INCOMPLETE" | "APPROVED" | "FLAGGED" | string;
}

