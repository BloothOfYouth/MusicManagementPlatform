<template>
    <div class="table">

        <div class="clearfix">
            <div class="crumbsTop">
                <i class="el-icon-tickets"></i> <span style="color: cornflowerblue; font-size: 18px; font-weight:bold">歌手</span> - 歌曲信息
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
                <el-image class="singer-img"
                          :src="HOST+singer.pic"
                          :preview-src-list="[HOST+singer.pic]"></el-image>
                <div class="singer-detail">
                    <strong>歌手：</strong>{{singer.name}}
                    <br>
                    <strong>简介：</strong>{{singer.introduction}}
                </div>
            </div>
        </div>

        <div class="container">
            <div class="handle-box">
                <el-button type="danger" @click="delSome">批量删除</el-button>
                <el-input v-model="queryInfo.query" placeholder="请输入歌曲名称" class="handle-input" @keyup.enter.native="getData">
                    <el-button slot="append" icon="el-icon-search" @click="getData"></el-button>
                </el-input>
                <el-button type="primary" @click="centerDialogVisible = true">添加歌曲</el-button>
                <el-tooltip class="item" effect="dark" content="返回" placement="bottom">
                    <el-button icon="el-icon-back" class="back" circle @click="back"></el-button>
                </el-tooltip>
            </div>
        </div>

        <el-table border style="width: 100%" height="680" stripe :data="songs" @selection-change="handleSelectionChange">
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
            <el-table-column label="资源更新" align="center" width="120px">
                <template slot-scope="scope">
                    <el-upload :action="HOST+'/song/updateSongPic/'+scope.row.id" :before-upload="beforeAvatorUpload"
                               :on-success="handleAvatorSuccess" accept=".jpeg,.png,.jpg" ref="uploadPic" style="margin-bottom: 8px">
                        <el-button size="mini" round icon="el-icon-picture-outline">更新图片</el-button>
                    </el-upload>
                    <el-upload :action="HOST+'/song/updateSongUrl/'+scope.row.id" :before-upload="beforeMusicUpload"
                               :on-success="handleMusicSuccess" accept=".mp3" ref="uploadMp3">
                        <el-button size="mini" round icon="el-icon-headset">更新歌曲</el-button>
                    </el-upload>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="170px">
                <template slot-scope="scope">
                    <el-button type="primary" icon="el-icon-edit" round size="small" @click="handleEdit(scope.row.id)">编辑</el-button>
                    <el-popconfirm
                            confirm-button-text='好的'
                            cancel-button-text='不用了'
                            icon="el-icon-info"
                            icon-color="red"
                            :title="'确定要删除[ '+scope.row.name+' ]歌曲?'"
                            @confirm="handleDelete(scope.row.id)"
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

        <el-dialog title="添加歌曲" :visible.sync="centerDialogVisible" width="400px" center="center" @close="cancelAdd">
            <el-form :model="registerForm" :rules="registerFormRule" ref="registerFormRef" label-width="65px" @keyup.enter.native="addSong" id="addForm">
                <input type="hidden" name="singerId" v-model="registerForm.singerId"></input>
                <el-form-item prop="name" label="歌名" size="mini">
                    <el-input name="name" v-model="registerForm.name" placeholder="歌名"></el-input>
                </el-form-item>
                <el-form-item prop="introduction" label="简介" size="mini">
                    <el-input name="introduction" v-model="registerForm.introduction" placeholder="简介"></el-input>
                </el-form-item>
                <el-form-item prop="lyric" label="歌词" size="mini">
                    <el-input name="lyric" v-model="registerForm.lyric" placeholder="歌词" type="textarea"></el-input>
                </el-form-item>
                <el-form-item prop="song" label="歌曲" size="mini">
                    <el-upload
                            id="upload"
                            :action="HOST+'/song/add'"
                            :auto-upload="false"
                            :limit="1"
                            ref="upload"
                            :on-change="beforeMusicUpload"
                            accept=".mp3">
                        <el-button size="small" type="primary">点击上传</el-button>
                        <div slot="tip" class="el-upload__tip">只支持上传mp3文件</div>
                    </el-upload>
                </el-form-item>
            </el-form>
            <span slot="footer">
                <el-button @click="cancelAdd">取 消</el-button>
                <el-button type="primary" @click="addSong">确 定</el-button>
            </span>
        </el-dialog>

        <el-dialog title="编辑歌曲" :visible.sync="editDialogVisible" width="400px" center="center">
            <el-form :model="editForm" :rules="registerFormRule" ref="editFormRef" label-width="65px" @keyup.enter.native="editSong">
                <el-form-item prop="name" label="歌名" size="mini">
                    <el-input name="name" v-model="editForm.name" placeholder="歌名"></el-input>
                </el-form-item>
                <el-form-item prop="introduction" label="简介" size="mini">
                    <el-input name="introduction" v-model="editForm.introduction" placeholder="简介"></el-input>
                </el-form-item>
                <el-form-item prop="lyric" label="歌词" size="mini">
                    <el-input name="lyric" v-model="editForm.lyric" placeholder="歌词" type="textarea"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer">
                <el-button @click="editDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="editSong">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import { me } from '../assets/js/me.js'
    import Aplayer from 'vue-aplayer'
    import '../assets/js/iconfont.js'
    export default {
        name: "SongPage",
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
                    singerId: '',
                    name: '',
                    introduction: '',
                    lyric: '',
                },
                singer: {

                },
                editForm: {

                },
                songs: [

                ],
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
                }
            }
        },
        methods: {
            addSong() {
                this.$refs.registerFormRef.validate(valid => {
                    var that = this;
                    if (valid) {
                        let formData = new FormData(document.getElementById("addForm"));
                        if (!formData.get("lyric")) {
                            formData.set("lyric","[00:00:00]暂无歌词")
                        }
                        let req = new XMLHttpRequest();
                        req.onreadystatechange = function () {
                            //req.readyState == 4 获取到返回的完整数据
                            //req.status == 200 和后台正常交互完成
                            if (req.readyState == 4 && req.status == 200) {
                                let res = JSON.parse(req.response);
                                if (res.result == "success"){
                                    that.$notify({
                                        title: "成功",
                                        message: res.message,
                                        type: "success"
                                    })
                                    that.getData()
                                    that.$refs.registerFormRef.resetFields()
                                }else {
                                    that.$notify({
                                        title: "错误",
                                        message: res.message,
                                        type: "error"
                                    })
                                }
                            }
                            that.$refs.upload.clearFiles()
                        }
                        req.open("post",this.HOST+"/song/add",false)
                        req.send(formData)
                    }else {
                        return;
                    }
                    this.centerDialogVisible = false
                })
            },
            cancelAdd() {
                this.$refs.registerFormRef.resetFields()
                this.$refs.upload.clearFiles()
                document.getElementById("upload").firstChild.lastChild.value = ""
                this.centerDialogVisible = false
            },
            getData() {
                this.$http.post("song/get/"+this.$route.params.id,this.queryInfo).then(res => {
                    this.songs = res.data.list
                    this.total = res.data.total
                    this.queryInfo.pageNum = res.data.pageNum
                    if (this.songs != null && this.songs.length == 0 && this.queryInfo.pageNum != 1) {
                        this.queryInfo.pageNum = 1
                        this.getData()
                    }
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
            handleEdit(id) {
                this.editDialogVisible = true
                this.$http.get("song/getOne/"+id).then(res => {
                    if (res.data.result == "success") {
                        this.editForm = res.data.message
                    }
                })
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
            handleDelete(id) {
                this.$http.delete("song/delete/"+id).then(res => {
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
                        ids.push(item.id)
                    }
                    this.$http.post("song/delSome",{"ids":ids}).then(res => {
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
            getOneSinger(id) {
                this.$http.get("singer/getOne/"+id).then(res => {
                    this.singer = res.data.message
                    this.registerForm.singerId = this.singer.id
                    this.playMusic.author = this.singer.name
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
                this.playMusic.artist = this.singer.name
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
            }
        },
        created() {
            this.getData()
            this.getOneSinger(this.$route.params.id)
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
    .singer-img {
        display: inline-block;
        float: left;
        width: 130px;
        height: 90px;
        border-radius: 5px;
    }
    .singer-detail {
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
