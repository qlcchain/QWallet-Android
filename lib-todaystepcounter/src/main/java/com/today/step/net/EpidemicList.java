package com.today.step.net;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class EpidemicList extends BaseBack {


    private List<RecordListBean> recordList;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RecordListBean> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<RecordListBean> recordList) {
        this.recordList = recordList;
    }

    public static class RecordListBean implements Parcelable{
        /**
         * qgasAmount : 0.01012844
         * transfer : {"transferStatus":"NEW","txid":"","fromAddress":"qlc_1q8xk7hdo8p3wym8wpb71zj4h1dmuby4twttm8o3h5ttae38dgrddo195e38","toAddress":"qlc_3cjtqyzto1uwcoh15itbpd9o1hq4bof7u8grkhkmay1t85mfk1o41m1z514b"}
         * days : 1
         * id : 4bc71f7b7cdc4434b0424927a88fd5f5
         * stepNumber : 0
         * qlcAmount : 100.0
         * status : AWARDED
         * createDate : 2020-04-16 10:57:24
         */

        private double qgasAmount;
        private TransferBean transfer;
        private int days;
        private String id;
        private int stepNumber;
        private double qlcAmount;
        private String status;
        private String createDate;

        protected RecordListBean(Parcel in) {
            qgasAmount = in.readDouble();
            transfer = in.readParcelable(TransferBean.class.getClassLoader());
            days = in.readInt();
            id = in.readString();
            stepNumber = in.readInt();
            qlcAmount = in.readDouble();
            status = in.readString();
            createDate = in.readString();
        }

        public static final Creator<RecordListBean> CREATOR = new Creator<RecordListBean>() {
            @Override
            public RecordListBean createFromParcel(Parcel in) {
                return new RecordListBean(in);
            }

            @Override
            public RecordListBean[] newArray(int size) {
                return new RecordListBean[size];
            }
        };

        public double getQgasAmount() {
            return qgasAmount;
        }

        public void setQgasAmount(double qgasAmount) {
            this.qgasAmount = qgasAmount;
        }

        public TransferBean getTransfer() {
            return transfer;
        }

        public void setTransfer(TransferBean transfer) {
            this.transfer = transfer;
        }

        public int getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getStepNumber() {
            return stepNumber;
        }

        public void setStepNumber(int stepNumber) {
            this.stepNumber = stepNumber;
        }

        public double getQlcAmount() {
            return qlcAmount;
        }

        public void setQlcAmount(double qlcAmount) {
            this.qlcAmount = qlcAmount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(qgasAmount);
            dest.writeParcelable(transfer, flags);
            dest.writeInt(days);
            dest.writeString(id);
            dest.writeInt(stepNumber);
            dest.writeDouble(qlcAmount);
            dest.writeString(status);
            dest.writeString(createDate);
        }

        public static class TransferBean implements Parcelable {
            /**
             * transferStatus : NEW
             * txid :
             * fromAddress : qlc_1q8xk7hdo8p3wym8wpb71zj4h1dmuby4twttm8o3h5ttae38dgrddo195e38
             * toAddress : qlc_3cjtqyzto1uwcoh15itbpd9o1hq4bof7u8grkhkmay1t85mfk1o41m1z514b
             */

            private String transferStatus;
            private String txid;
            private String fromAddress;
            private String toAddress;

            protected TransferBean(Parcel in) {
                transferStatus = in.readString();
                txid = in.readString();
                fromAddress = in.readString();
                toAddress = in.readString();
            }

            public static final Creator<TransferBean> CREATOR = new Creator<TransferBean>() {
                @Override
                public TransferBean createFromParcel(Parcel in) {
                    return new TransferBean(in);
                }

                @Override
                public TransferBean[] newArray(int size) {
                    return new TransferBean[size];
                }
            };

            public String getTransferStatus() {
                return transferStatus;
            }

            public void setTransferStatus(String transferStatus) {
                this.transferStatus = transferStatus;
            }

            public String getTxid() {
                return txid;
            }

            public void setTxid(String txid) {
                this.txid = txid;
            }

            public String getFromAddress() {
                return fromAddress;
            }

            public void setFromAddress(String fromAddress) {
                this.fromAddress = fromAddress;
            }

            public String getToAddress() {
                return toAddress;
            }

            public void setToAddress(String toAddress) {
                this.toAddress = toAddress;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(transferStatus);
                dest.writeString(txid);
                dest.writeString(fromAddress);
                dest.writeString(toAddress);
            }
        }
    }
}
