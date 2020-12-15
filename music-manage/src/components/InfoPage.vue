<template>
    <div>
        <el-row :gutter="20" class="mgb20">
            <el-col :span="6">
                <el-card>
                    <div class="grid-content">
                        <div class="grid-cont-center">
                            <div class="grid-num" style="color: #87cefa">{{userCount}}</div>
                            <div>用户总数</div>
                        </div>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card>
                    <div class="grid-content">
                        <div class="grid-cont-center">
                            <div class="grid-num" style="color: #ffc0cb">{{songCount}}</div>
                            <div>歌曲总数</div>
                        </div>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card>
                    <div class="grid-content">
                        <div class="grid-cont-center">
                            <div class="grid-num" style="color: #90ee90">{{singerCount}}</div>
                            <div>歌手数量</div>
                        </div>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card>
                    <div class="grid-content">
                        <div class="grid-cont-center">
                            <div class="grid-num" style="color: #ffc66d">{{songListCount}}</div>
                            <div>歌单总数</div>
                        </div>
                    </div>
                </el-card>
            </el-col>
        </el-row>
        <el-row :gutter="20" class="mgb20">
            <el-col :span="12">
                <el-card>
                    <div slot="header" class="clearfix">
                        <span style="font-size: 18px; color: darkgray">用户性别比例</span>
                    </div>
                    <ve-pie :data="userSex" :theme="options"></ve-pie>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card>
                    <div slot="header" class="clearfix">
                        <span style="font-size: 18px; color: darkgray">歌曲类型分布</span>
                    </div>
                    <ve-histogram :data="songStyle"></ve-histogram>
                </el-card>
            </el-col>
        </el-row>
        <el-row class="mgb20">
            <el-col :span="24">
                <el-card>
                    <div slot="header" class="clearfix">
                        <span style="font-size: 18px; color: darkgray">歌手性别比例</span>
                    </div>
                    <ve-pie :data="singerSex"></ve-pie>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    export default {
        name: "InfoPage",
        data() {
            return {
                userCount: 0, //用户总数
                songCount: 0, //歌曲总数
                singerCount: 0, //歌手数量
                songListCount: 0, //歌单数量
                userSex: {
                    columns: ['性别','总数'],
                    rows: [
                        {'性别': '男','总数': 0},
                        {'性别': '女','总数': 0},
                    ]
                },
                songStyle: {
                    columns: ['风格','总数'],
                    rows: [
                        {'风格': '华语','总数': 0},
                        {'风格': '粤语','总数': 0},
                        {'风格': '欧美','总数': 0},
                        {'风格': '日韩','总数': 0},
                        {'风格': 'BGM','总数': 0},
                        {'风格': '轻音乐','总数': 0},
                        {'风格': '乐器','总数': 0},
                    ]
                },
                singerSex: {
                  columns: ['性别','总数'],
                  rows: [
                      {'性别':'女','总数': 0},
                      {'性别':'男','总数': 0},
                      {'性别':'组合','总数': 0},
                      {'性别':'不明','总数': 0},
                  ]
                },
                options: {
                    color: ['#87cefa','#ffc0cb']
                }
            }
        },
        methods: {
            getAllCount() {
                this.$http.get("info/getAllCount").then(res => {
                    this.userCount = res.data.message.userCount;
                    this.songCount = res.data.message.songCount;
                    this.singerCount = res.data.message.singerCount;
                    this.songListCount = res.data.message.songListCount;

                    this.userSex.rows[0]["总数"] = res.data.message.girlsCount;
                    this.userSex.rows[1]["总数"] = res.data.message.boysCount;

                    this.songStyle.rows[0]["总数"] = res.data.message.style.huayu;
                    this.songStyle.rows[1]["总数"] = res.data.message.style.yueyu;
                    this.songStyle.rows[2]["总数"] = res.data.message.style.omei;
                    this.songStyle.rows[3]["总数"] = res.data.message.style.rihan;
                    this.songStyle.rows[4]["总数"] = res.data.message.style.BGM;
                    this.songStyle.rows[5]["总数"] = res.data.message.style.qingyinyue;
                    this.songStyle.rows[6]["总数"] = res.data.message.style.yueqi;

                    this.singerSex.rows[0]["总数"] = res.data.message.singerSex.nv;
                    this.singerSex.rows[1]["总数"] = res.data.message.singerSex.nan;
                    this.singerSex.rows[2]["总数"] = res.data.message.singerSex.zuhe;
                    this.singerSex.rows[3]["总数"] = res.data.message.singerSex.buming;
                })
            },
        },
        created() {
            this.getAllCount();
        }
    }
</script>

<style scoped lang="less">
    .grid-content {
        display: flex;
        align-items: center;
        height: 50px;
    }
    .grid-cont-center {
        flex: 1;
        text-align: center;
        font-size: 14px;
        color: darkgray;
    }
    .grid-num {
        font-size: 26px;
        font-width: bold;
    }
</style>
