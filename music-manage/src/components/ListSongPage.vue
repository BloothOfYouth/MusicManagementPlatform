<template>
    <div class="table">

        <div class="clearfix">
            <div class="crumbsTop">
                <i class="el-icon-tickets"></i> <span style="color: cornflowerblue; font-size: 18px; font-weight:bold">歌单</span> - 歌曲信息
            </div>
            <aplayer autoplay theme="#409EFF" class="aplayer" ref="player" :showLrc="true" :music="{
                    title: this.playMusic.title,
                    artist: this.playMusic.artist,
                    url: this.playMusic.url,
                    pic: this.playMusic.pic,
                    lrc: this.playMusic.lrc
                }">
            </aplayer>
            <div class="crumbsBottom">
                <el-image class="songList-img"
                          :src="HOST+songList.pic"
                          :preview-src-list="[HOST+songList.pic]"></el-image>
                <div class="songList-detail">
                    <strong>歌单：</strong>{{songList.title}}
                    <br>
                    <strong>简介：</strong>{{songList.introduction}}
                </div>
            </div>
        </div>

        <div class="container">
            <div class="handle-box">
                <el-button type="danger" @click="delSome">批量删除</el-button>
                <el-input v-model="queryInfo.query" placeholder="请输入歌曲名称" class="handle-input" @keyup.enter.native="getData">
                    <el-button slot="append" icon="el-icon-search" @click="getData"></el-button>
                </el-input>
                <el-button type="primary" @click="handleAddSong">添加歌曲</el-button>
                <el-tooltip class="item" effect="dark" content="返回" placement="bottom">
                    <el-button icon="el-icon-back" class="back" circle @click="back"></el-button>
                </el-tooltip>
            </div>
        </div>

        <el-table border style="width: 100%" height="600" stripe :data="songs" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="40px"></el-table-column>
            <el-table-column label="歌曲图片" width="150px" align="center">
                <template slot-scope="scope">
                    <div class="song-img">
                        <el-image :src="HOST+scope.row.pic" :preview-src-list="[HOST+scope.row.pic]"></el-image>
                    </div>
                    <div class="play" v-if="toggle == scope.row.id" @click="pauseSong(scope.row)">
                        <div>
                            <svg class="icon">
                                <use xlink:href="#icon-zanting"></use>
                            </svg>
                        </div>
                    </div>
                    <div class="play" v-if="toggle != scope.row.id" @click="setSongUrl(scope.row)">
                        <div @click="pauseSong(scope.row)">
                            <svg class="icon">
                                <use xlink:href="#icon-bofanganniu"></use>
                            </svg>
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="歌手" width="120px" align="center">
                <template slot-scope="scope">
                    <el-link icon="el-icon-user" @click="songEdit(scope.row.singer.id)" type="primary">{{scope.row.singer.name}}</el-link>
                </template>
            </el-table-column>
            <el-table-column prop="name" label="歌名" width="120px" align="center"></el-table-column>
            <el-table-column prop="introduction" label="简介" width="110px" align="center"></el-table-column>
            <el-table-column label="歌词" align="center">
                <template slot-scope="scope">
                    <ul style="height: 100px; overflow: auto">
                        <li v-for="(item,index) in parseLyric(scope.row.lyric)" :key="index">
                            {{item}}
                        </li>
                    </ul>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="170px">
                <template slot-scope="scope">
                    <el-popconfirm
                            confirm-button-text='好的'
                            cancel-button-text='不用了'
                            icon="el-icon-info"
                            icon-color="red"
                            :title="'确定要删除[ '+scope.row.name+' ]歌曲?'"
                            @confirm="handleDelete(scope.row.listSongId)"
                    >
                        <el-button slot="reference" type="danger" icon="el-icon-delete" round size="small">删除</el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>

        <div class="paging">
            <el-pagination
                    background
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="queryInfo.pageNum"
                    :page-sizes="[3, 5, 10]"
                    :page-size="queryInfo.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>

        <el-dialog title="添加歌曲" width="770px" :visible.sync="centerDialogVisible" center="center" @close="cancelAdd">
            <div class="container">
                <div>
                    <el-input v-model="addDialog.queryInfo.query" placeholder="请输入歌曲名称" class="handle-input" @keyup.enter.native="getAllSong">
                        <el-button slot="append" icon="el-icon-search" @click="getAllSong"></el-button>
                    </el-input>
                    <el-button type="primary" @click="AddSomeSong">批量添加</el-button>
                </div>
            </div>

            <el-table border stripe :data="addDialog.songs" @selection-change="handleAddDialogSelectionChange">
                <el-table-column :selectable="canSelect" type="selection" width="40px"></el-table-column>
                <el-table-column label="歌曲图片" width="150px" align="center">
                    <template slot-scope="scope">
                        <div class="song-img">
                            <el-image :src="HOST+scope.row.pic" :preview-src-list="[HOST+scope.row.pic]"></el-image>
                        </div>
                        <div class="play" v-if="toggle == scope.row.id" @click="pauseSong(scope.row)">
                            <div>
                                <svg class="icon">
                                    <use xlink:href="#icon-zanting"></use>
                                </svg>
                            </div>
                        </div>
                        <div class="play" v-if="toggle != scope.row.id" @click="setSongUrl(scope.row)">
                            <div @click="pauseSong(scope.row)">
                                <svg class="icon">
                                    <use xlink:href="#icon-bofanganniu"></use>
                                </svg>
                            </div>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="歌手" width="120px" align="center">
                    <template slot-scope="scope">
                        <el-link icon="el-icon-user" @click="songEdit(scope.row.singer.id)" type="primary">{{scope.row.singer.name}}</el-link>
                    </template>
                </el-table-column>
                <el-table-column prop="name" label="歌名" width="120px" align="center"></el-table-column>
                <el-table-column prop="introduction" label="简介" align="center"></el-table-column>
                <el-table-column label="操作" align="center" width="170px">
                    <template slot-scope="scope">
                        <el-button v-if="!checkIsAddSong(scope.row.id)" type="primary" icon="el-icon-edit" round size="small" @click="handleAddOne(scope.row.id)">添加</el-button>
                        <span v-if="checkIsAddSong(scope.row.id)" style="color: chocolate">已添加</span>
                    </template>
                </el-table-column>
            </el-table>

            <div class="paging">
                <el-pagination
                        background
                        @size-change="addDialogHandleSizeChange"
                        @current-change="addDialogHandleCurrentChange"
                        :current-page="addDialog.queryInfo.pageNum"
                        :page-sizes="[3, 5, 10]"
                        :page-size="addDialog.queryInfo.pageSize"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="addDialog.total">
                </el-pagination>
            </div>
        </el-dialog>

    </div>
</template>

<script>
    import { me } from '../assets/js/me.js'
    import Aplayer from 'vue-aplayer'
    import '../assets/js/iconfont.js'
    export default {
        name: "ListSongPage",
        data() {
            let checkSong = (rule, value, callback) => {
                let value1 = document.getElementById("upload").firstChild.lastChild.value
                if (!value1) {
                    callback(new Error('请添加mp3文件'));
                }else {
                    callback();
                }
            };
            return {
                centerDialogVisible: false,
                editDialogVisible: false,
                registerForm: {
                    songListId: '',
                    name: '',
                    introduction: '',
                    lyric: '',
                },
                songList: {

                },
                editForm: {

                },
                songs: [

                ],
                addDialog: {
                    songs: [],
                    queryInfo: {
                        query: '',
                        pageNum: 1,
                        pageSize: 3
                    },
                    total: 0,
                    multipleSelection: []
                },
                queryInfo: {
                    query: '',
                    pageNum: 1,
                    pageSize: 5
                },
                total: 0,
                registerFormRule: {
                    name: [
                        { required: true, message: '姓名为必填项', trigger: 'blur' },
                    ],
                    introduction: [
                        { required: true, message: '简介为必填项', trigger: 'blur' },
                    ],
                    song: [
                        { required: true, validator: checkSong, trigger: 'blur' }
                    ]
                },
                HOST: this.$http.defaults.baseURL,
                multipleSelection: [],  // 哪些选项打勾
                toggle: '',  // 播放器的图标状态
                playMusic: {
                    title: '无',
                    artist: '无',
                    url: '无',
                    pic: '无',
                    lrc: '无'
                },
                songIds: [] // 通过songListId查询歌曲的SongId集合
            }
        },
        methods: {
            cancelAdd() {
                this.centerDialogVisible = false
            },
            getData() {
                this.$http.post("listSong/get/"+this.$route.params.id,this.queryInfo).then(res => {
                    this.songs = res.data.list
                    this.total = res.data.total
                    this.queryInfo.pageNum = res.data.pageNum
                    if (this.songs != null && this.songs.length == 0 && this.queryInfo.pageNum != 1) {
                        this.queryInfo.pageNum = 1
                        this.getData()
                    }
                })

                this.$http.get("listSong/getSongIds/"+this.$route.params.id).then(res => {
                    this.songIds = res.data.message
                })
            },
            beforeMusicUpload(file,fileList) {
                let index = file.name.lastIndexOf('\.')
                let type = file.name.substring(index+1,file.name.length)
                const isMP3 = (type === 'mp3')
                if(!isMP3) {
                    this.$message({
                        showClose: true,
                        message: '只支持上传mp3文件',
                        type: 'error'
                    })
                    this.$refs.uploadMp3.clearFiles()
                    document.getElementById("upload").firstChild.lastChild.value = ""
                }
            },
            handleMusicSuccess(res) {
                me.methods.handleFileSuccess(res,this)
            },
            beforeAvatorUpload(file) {
                me.methods.beforeAvatorUpload(file,this)
            },
            handleAvatorSuccess(res) {
                me.methods.handleFileSuccess(res,this)
            },
            // 改变最大显示数触发动作
            handleSizeChange(newPageSize) {
                this.queryInfo.pageSize = newPageSize;
                this.getData();
            },
            // 当前页触发动作
            handleCurrentChange(newPageNum) {
                this.queryInfo.pageNum = newPageNum;
                this.getData();
            },
            addDialogHandleSizeChange(newPageSize) {
                this.addDialog.queryInfo.pageSize = newPageSize;
                this.getAllSong()
            },
            addDialogHandleCurrentChange(newPageNum) {
                this.addDialog.queryInfo.pageNum = newPageNum;
                this.getAllSong()
            },
            editSong() {
                this.$refs.editFormRef.validate(valid => {
                    if (valid) {
                        // let birth = this.editForm.birth;
                        // this.editForm.birth = birth.getFullYear()+'-'+(birth.getMonth()+1)+'-'+birth.getDate()
                        this.$http.put("song/update",this.editForm).then(res => {
                            if (res.data.result == "success"){
                                this.$notify({
                                    title: "成功",
                                    message: res.data.message,
                                    type: "success"
                                })
                                this.getData()
                            }else {
                                this.$notify({
                                    title: "错误",
                                    message: res.data.message,
                                    type: "error"
                                })
                            }
                        })
                    }else {
                        return;
                    }
                    this.editDialogVisible = false
                })
            },
            handleAddOne(id) {
                let listSongs = []
                listSongs.push({"songId":id, "songListId":this.$route.params.id})

                this.$http.post("listSong/addSome",{"listSongs":listSongs}).then(res => {
                    if (res.data.result == "success"){
                        this.$notify({
                            title: "成功",
                            message: res.data.message,
                            type: "success"
                        })
                        this.getData()
                    }else {
                        this.$notify({
                            title: "错误",
                            message: res.data.message,
                            type: "error"
                        })
                    }
                })
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            handleAddDialogSelectionChange(val) {
                this.addDialog.multipleSelection = val;
            },
            handleDelete(id) {
                this.$http.delete("listSong/delete/"+id).then(res => {
                    if (res.data.result == "success"){
                        this.$notify({
                            title: "成功",
                            message: res.data.message,
                            type: "success"
                        })
                        this.getData()
                    }else {
                        this.$notify({
                            title: "错误",
                            message: res.data.message,
                            type: "error"
                        })
                    }
                })
            },
            delSome() {
                if (this.multipleSelection.length == 0) {
                    this.$alert("请选择要批量删除的选项","提示",{
                        type: "info ",
                        confirmButtonText: "确定"
                    })
                    return;
                }
                this.$confirm('确定要批量删除吗？',{
                    type: "warning"
                }).then(ok => {
                    let ids = []
                    for (let item of this.multipleSelection) {
                        ids.push(item.listSongId)
                    }
                    this.$http.post("listSong/delSome",{"ids":ids}).then(res => {
                        if (res.data.result == "success"){
                            this.$notify({
                                title: "成功",
                                message: res.data.message,
                                type: "success"
                            })
                            this.getData()
                        }else {
                            this.$notify({
                                title: "错误",
                                message: res.data.message,
                                type: "error"
                            })
                        }
                    })
                    this.multipleSelection = []
                }).catch(no => {

                })
            },
            songEdit(id) {
                this.$router.push({
                    name: "SongPage",
                    params: {id: id}
                })
            },
            getOneSongList(id) {
                this.$http.get("songList/getOne/"+id).then(res => {
                    this.songList = res.data.message
                })
            },
            // 解析歌词
            parseLyric(text) {
                let lines = text.split("\n")
                let pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})\]/g
                let result = []
                for (let item of lines) {
                    let value = item.replace(pattern,"");
                    result.push(value)
                }
                return result
            },
            setSongUrl(song) {
                this.playMusic.title = song.name
                this.playMusic.artist = song.singer.name
                this.playMusic.url = this.HOST+song.url
                this.playMusic.pic = this.HOST+song.pic
                this.playMusic.lrc = song.lyric

                this.$http.get(this.playMusic.url).then(res => {
                    this.$refs.player.play()
                    this.toggle = song.id
                })
            },
            pauseSong() {
                this.toggle = "";
                this.$refs.player.pause()
            },
            back() {
                this.$router.go(-1)
            },
            getAllSong() {
                this.$http.post("song/getAll",this.addDialog.queryInfo).then(res => {
                    this.addDialog.songs = res.data.list
                    this.addDialog.total = res.data.total
                    this.addDialog.queryInfo.pageNum = res.data.pageNum
                })
            },
            handleAddSong() {
                this.centerDialogVisible = true
                this.getAllSong()
            },
            AddSomeSong() {
                if (this.addDialog.multipleSelection.length == 0) {
                    this.$alert("请选择要批量添加的选项","提示",{
                        type: "info ",
                        confirmButtonText: "确定"
                    })
                    return;
                }

                let listSongs = []
                for (let item of this.addDialog.multipleSelection) {
                    listSongs.push({"songId":item.id, "songListId":this.$route.params.id})
                }

                this.$http.post("listSong/addSome",{"listSongs":listSongs}).then(res => {
                    if (res.data.result == "success"){
                        this.$notify({
                            title: "成功",
                            message: res.data.message,
                            type: "success"
                        })
                        this.getData()
                    }else {
                        this.$notify({
                            title: "错误",
                            message: res.data.message,
                            type: "error"
                        })
                    }
                })
                this.addDialog.multipleSelection = []
            },
            // 确认是否添加此歌曲
            checkIsAddSong(id) {
                let i = this.songIds.length;
                while (i--) {
                    if (this.songIds[i] === id) {
                        return true
                    }
                }
                return false
            },
            canSelect(row) {
                let flag = !this.checkIsAddSong(row.id);
                return flag
            }
        },
        created() {
            this.getData()
            this.getOneSongList(this.$route.params.id)
        },
        watch: {

        },
        components: {
            Aplayer
        }
    }
</script>

<style scoped lang="less">
    .handle-box {
        display: flex;
        margin-bottom: 20px;
    }
    .song-img {
        width: 100%;
        height: 87px;
        border-radius: 5px;
        margin-bottom: 5px;
        overflow: hidden;
        .el-image {
            width: 100%;
            height: 100%;
        }
    }
    .handle-input {
        width: 500px;
        margin-right: 20px;
        margin-left: 20px;
    }
    .paging {
        display: flex;
        justify-content: center;
        margin-top: 15px;
    }
    .crumbsTop {
        display: block;
        background-color: #ffffff;
        text-align: center;
    }
    .crumbsBottom {
        margin-top: 10px;
        margin-bottom: 10px;
        height: 90px;
        display: block;
    }
    .songList-img {
        display: inline-block;
        float: left;
        width: 130px;
        height: 90px;
        border-radius: 5px;
    }
    .songList-detail {
        padding-left: 3px;
        line-height: 1.46;
        word-break: break-all;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 4;
        overflow: hidden;
    }
    .aplayer {
        /*position: fixed;*/
        /*top: -0.3%;*/
        /*left: 50%;*/
        /*width: 500px;*/
        /*margin-left: -250px;*/
        /*z-index: 100;*/
    }
    .back {

    }
    .play {
        position: absolute;
        z-index: 100;
        width: 80px;
        height: 80px;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        top: 17px;
        left: 37px;
    }
    .icon {
        width: 2em;
        height: 2em;
        color: rgba(233,233,233,0.8);
        fill: currentColor;
        overflow: hidden;
    }
</style>
