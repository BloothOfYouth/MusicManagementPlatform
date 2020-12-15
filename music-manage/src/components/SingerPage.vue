<template>
    <div class="table">

        <div class="container">
            <div class="handle-box">
                <el-button type="danger" @click="delSome">批量删除</el-button>
                <el-input v-model="queryInfo.query" placeholder="请输入歌手名称" class="handle-input" @keyup.enter.native="getData">
                    <el-button slot="append" icon="el-icon-search" @click="getData"></el-button>
                </el-input>
                <el-button type="primary" @click="centerDialogVisible = true">添加歌手</el-button>
            </div>
        </div>

        <el-table border style="width: 100%" height="680" stripe :data="singers" @selection-change="handleSelectionChange" ref="singerTable">
            <el-table-column :selectable="canSelect" type="selection" width="40px"></el-table-column>
            <el-table-column label="歌手图片" width="150px" align="center">
                <template slot-scope="scope">
                    <div class="singer-img">
                        <el-image :src="HOST+scope.row.pic" :preview-src-list="[HOST+scope.row.pic]"></el-image>
                    </div>
                    <el-upload :action="uploadUrl(scope.row.id)" :before-upload="beforeAvatorUpload"
                               :on-success="handleAvatorSuccess" accept=".jpeg,.png,.jpg" ref="uploadPic">
                        <el-button size="mini" round icon="el-icon-picture-outline">更新图片</el-button>
                    </el-upload>
                </template>
            </el-table-column>
            <el-table-column prop="name" label="歌手" width="120px" align="center"></el-table-column>
            <el-table-column label="性别" align="center" width="50px">
                <template slot-scope="scope">
                    {{ changeSex(scope.row.sex) }}
                </template>
            </el-table-column>
            <el-table-column label="生日" width="100px" align="center">
                <template slot-scope="scope">
                    {{scope.row.birth | formatDate }}
                </template>
            </el-table-column>
            <el-table-column prop="location" label="地区" width="100px" align="center"></el-table-column>
            <el-table-column label="简介" align="center">
                <template slot-scope="scope">
                    <p style="height: 100px;overflow: auto">{{ scope.row.introduction }}</p>
                </template>
            </el-table-column>
            <el-table-column label="歌曲管理" width="120px" align="center">
                <template slot-scope="scope">
                    <el-button size="mini" round icon="el-icon-headset" @click="songEdit(scope.row.id)">歌曲管理</el-button>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="180px">
                <template slot-scope="scope">
                    <el-button type="primary" icon="el-icon-edit" round size="small" @click="handleEdit(scope.row.id)">编辑</el-button>
                    <el-tooltip effect="dark" :content="checkHaveSong(scope.row)?'此歌手有歌曲不可删除':'可以删除'" placement="bottom">
                        <el-popconfirm
                                confirm-button-text='好的'
                                cancel-button-text='不用了'
                                icon="el-icon-info"
                                icon-color="red"
                                :title="'确定要删除[ '+scope.row.name+' ]歌手?'"
                                @confirm="handleDelete(scope.row.id)"
                        >
                            <el-button slot="reference" type="danger" icon="el-icon-delete" round :disabled="checkHaveSong(scope.row)" size="small">删除</el-button>
                        </el-popconfirm>
                    </el-tooltip>
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

        <el-dialog title="添加歌手" :visible.sync="centerDialogVisible" width="400px" center="center" @close="cancelAdd">
            <el-form :model="registerForm" :rules="registerFormRule" ref="registerFormRef" label-width="65px" @keyup.enter.native="addSinger">
                <el-form-item prop="name" label="歌手名" size="mini">
                    <el-input v-model="registerForm.name" placeholder="歌手名"></el-input>
                </el-form-item>
                <el-form-item prop="sex" label="性别" size="mini">
                    <el-radio-group v-model="registerForm.sex">
                        <el-radio :label="0">女</el-radio>
                        <el-radio :label="1">男</el-radio>
                        <el-radio :label="2">组合</el-radio>
                        <el-radio :label="3">不明</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item prop="birth" label="生日" size="mini">
                    <el-date-picker v-model="registerForm.birth" type="date" placeholder="选择日期" style="width: 100%"></el-date-picker>
                </el-form-item>
                <el-form-item prop="location" label="地区" size="mini">
                    <el-input v-model="registerForm.location" placeholder="地区"></el-input>
                </el-form-item>
                <el-form-item prop="introduction" label="简介" size="mini">
                    <el-input v-model="registerForm.introduction" placeholder="简介" type="textarea"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer">
                <el-button @click="cancelAdd">取 消</el-button>
                <el-button type="primary" @click="addSinger">确 定</el-button>
            </span>
        </el-dialog>

        <el-dialog title="编辑歌手" :visible.sync="editDialogVisible" width="400px" center="center">
            <el-form :model="editForm" :rules="registerFormRule" ref="editFormRef" label-width="65px" @keyup.enter.native="editSinger">
                <el-form-item prop="name" label="歌手名" size="mini">
                    <el-input v-model="editForm.name" placeholder="歌手名"></el-input>
                </el-form-item>
                <el-form-item prop="sex" label="性别" size="mini">
                    <el-radio-group v-model="editForm.sex">
                        <el-radio :label="0">女</el-radio>
                        <el-radio :label="1">男</el-radio>
                        <el-radio :label="2">组合</el-radio>
                        <el-radio :label="3">不明</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item prop="birth" label="生日" size="mini">
                    <el-date-picker v-model="editForm.birth" type="date" placeholder="选择日期" style="width: 100%"></el-date-picker>
                </el-form-item>
                <el-form-item prop="location" label="地区" size="mini">
                    <el-input v-model="editForm.location" placeholder="地区"></el-input>
                </el-form-item>
                <el-form-item prop="introduction" label="简介" size="mini">
                    <el-input v-model="editForm.introduction" placeholder="简介" type="textarea"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer">
                <el-button @click="editDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="editSinger">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import { me } from '../assets/js/me.js'
    export default {
        name: "SingerPage",
        data() {
            return {
                centerDialogVisible: false,
                editDialogVisible: false,
                registerForm: {
                    name: '',
                    sex: '',
                    pic: '',
                    birth: '',
                    location: '',
                    introduction: '',
                },
                editForm: {

                },
                singers: [

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
                    sex: [
                        { required: true, message: '性别为必填项', trigger: 'blur' },
                    ],
                    introduction: [
                        { required: true, message: '简介为必填项', trigger: 'blur' },
                    ]
                },
                HOST: this.$http.defaults.baseURL,
                multipleSelection: [],  // 哪些选项打勾
            }
        },
        methods: {
            addSinger() {
                this.$refs.registerFormRef.validate(valid => {
                    if (valid) {
                        // let birth = this.registerForm.birth;
                        // this.registerForm.birth = birth.getFullYear()+'-'+(birth.getMonth()+1)+'-'+birth.getDate()
                        // this.registerForm.pic = '/img/singerPic/hhh.jpg'
                        this.$http.post("singer/add",this.registerForm).then(res => {
                            if (res.data.result == "success"){
                                this.$notify({
                                    title: "成功",
                                    message: res.data.message,
                                    type: "success"
                                })
                                this.getData()
                                this.$refs.registerFormRef.resetFields()
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
                    this.centerDialogVisible = false
                })
            },
            cancelAdd() {
                this.$refs.registerFormRef.resetFields()
                this.centerDialogVisible = false
            },
            getData() {
                this.$http.post("singer/get",this.queryInfo).then(res => {
                    this.singers = res.data.list
                    this.total = res.data.total
                    this.queryInfo.pageNum = res.data.pageNum
                    if (this.singers != null && this.singers.length == 0 && this.queryInfo.pageNum != 1) {
                        this.queryInfo.pageNum = 1
                        this.getData()
                    }
                })
            },
            changeSex(value) {
                if (value == 0) {
                    return '女'
                }
                if (value == 1) {
                    return '男'
                }
                if (value == 2) {
                    return '组合'
                }
                if (value == 3) {
                    return '不明'
                }
                return value
            },
            uploadUrl(id) {
                return this.HOST+'/singer/updateSingerPic/'+id;
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
                this.$http.get("singer/getOne/"+id).then(res => {
                    if (res.data.result == "success") {
                        this.editForm = res.data.message
                    }
                })
            },
            editSinger() {
                this.$refs.editFormRef.validate(valid => {
                    if (valid) {
                        // let birth = this.editForm.birth;
                        // this.editForm.birth = birth.getFullYear()+'-'+(birth.getMonth()+1)+'-'+birth.getDate()
                        this.$http.put("singer/update",this.editForm).then(res => {
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
                this.$http.delete("singer/delete/"+id).then(res => {
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
                    this.$http.post("singer/delSome",{"ids":ids}).then(res => {
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
            checkHaveSong(item) {
                if (item.songCount != 0) {
                    return true
                }else {
                    return false
                }
            },
            canSelect(row) {
                let flag = !this.checkHaveSong(row);
                return flag
            }
        },
        created() {
            this.getData()
        },
        watch: {

        }
    }
</script>

<style scoped lang="less">
    .handle-box {
        margin-bottom: 20px;
    }
    .singer-img {
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
</style>
